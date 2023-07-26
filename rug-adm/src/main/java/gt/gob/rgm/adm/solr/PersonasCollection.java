package gt.gob.rgm.adm.solr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.common.SolrInputDocument;

import gt.gob.rgm.adm.dao.TramitesRepository;
import gt.gob.rgm.adm.model.RugPersonasH;
import gt.gob.rgm.adm.model.RugPersonasHPK;
import gt.gob.rgm.adm.model.RugRelTramPartes;
import gt.gob.rgm.adm.model.RugRelTramPartesPK;
import gt.gob.rgm.adm.model.Tramites;
import gt.gob.rgm.adm.service.RugPersonasHService;
import gt.gob.rgm.adm.service.RugRelTramPartesService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Stateless
public class PersonasCollection {
	@Inject
	private RugPersonasHService personasService;
	
	@Inject
	private RugRelTramPartesService partesService; 
	
	@Inject
	private TramitesRepository tramitesDao;
	
	private SolrService service;
	
	public PersonasCollection() {
		service = ServiceGenerator.createService(SolrService.class);
	}
	
	public void indexarTramite(Long idTramite) {
		SolrAdd root = new SolrAdd();
		
		RugRelTramPartes partesFilter = new RugRelTramPartes();
		if(idTramite != null) {
			// obtener el tramite basado en el tramite temporal
			Tramites tram = new Tramites();
			tram = tramitesDao.findByIdTemp(idTramite);
			
			RugRelTramPartesPK id = new RugRelTramPartesPK();
			id.setIdTramite(tram.getIdTramite());
			partesFilter.setId(id);
			List<RugRelTramPartes> partes = partesService.getPartes(partesFilter, null, null);
			for(RugRelTramPartes parte : partes) {
				List<SolrDoc> docs = this.indexarPersonas(parte.getId().getIdPersona());
				root.getDocs().addAll(docs);
			}
		} else {
			List<SolrDoc> docs = this.indexarPersonas(null);
			root.getDocs().addAll(docs);
		}
		
		try {
			File generated = new File("persona-list.xml");
			JAXBContext context = JAXBContext.newInstance(SolrAdd.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(root, generated);
			
			// enviar archivo a Solr
			//sendFileSolr(pHost, generated, core);
			uploadFile(generated);
			// eliminar archivo temporal
			//generated.delete();
        }  catch (JAXBException e) { 
            //e.printStackTrace(); 
        }

	}

	public List<SolrDoc> indexarPersonas(Long idPersona) {
		RugPersonasH personasFilter = new RugPersonasH();
		if(idPersona != null) {
			RugPersonasHPK id = new RugPersonasHPK();
			id.setIdPersona(idPersona);
			personasFilter.setId(id);
		}
		List<PersonaSolr> personas = personasService.listPersonas(personasFilter, null, null);
		//SolrAdd root = new SolrAdd();
		List<SolrDoc> docs = new ArrayList<>();
		/*String urlString = "http://localhost:8983/solr/rgm";
		HttpSolrClient solr = new HttpSolrClient.Builder(urlString).build();
		solr.setParser(new XMLResponseParser());*/
		for(PersonaSolr persona : personas) {
			SolrDoc doc = new SolrDoc();
			List<SolrField> fields = new ArrayList<>();
			fields.add(addFieldSolar(SolrField.ID_PERSONA, persona.getIdPersona().toString()));
			if(persona.getNombre() != null) {
				fields.add(addFieldSolar(SolrField.NOMBRE, persona.getNombre().trim()));
			}
			fields.add(addFieldSolar(SolrField.TIPO, persona.getTipoPersona()));
			if(persona.getNit() != null) {
				fields.add(addFieldSolar(SolrField.NIT, persona.getNit().trim()));
			}
			if(persona.getDocid() != null) {
				fields.add(addFieldSolar(SolrField.DOCID, persona.getDocid().trim()));
			}
			if(persona.getEmail() != null) {
				String email = persona.getEmail().trim();
				email = email.replaceAll("\\p{Cntrl}", "");
				fields.add(addFieldSolar(SolrField.EMAIL, email));
			}
			
			doc.setFields(fields);
            //root.getDocs().add(doc);
			docs.add(doc);
			/*SolrInputDocument document = new SolrInputDocument();
			document.addField(SolrField.ID_PERSONA, persona.getIdPersona());
			if(persona.getNombre() != null) {
				document.addField(SolrField.NOMBRE, persona.getNombre());
			}
			document.addField(SolrField.TIPO, persona.getTipoPersona());
			if(persona.getNit() != null) {
				document.addField(SolrField.NIT, persona.getNit());
			}
			if(persona.getDocid() != null) {
				document.addField(SolrField.DOCID, persona.getDocid());
			}
			document.addField(SolrField.EMAIL, persona.getEmail());
			
			try {
				solr.add(document);
				solr.commit();
			} catch (SolrServerException | IOException e) {
				e.printStackTrace();
			}*/
		}
		
		/*try {
			File generated = new File("persona-list.xml");
			JAXBContext context = JAXBContext.newInstance(SolrAdd.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(root, generated);
			
			// enviar archivo a Solr
			//sendFileSolr(pHost, generated, core);
			uploadFile(generated);
			// eliminar archivo temporal
			//generated.delete();
        }  catch (JAXBException e) { 
            e.printStackTrace(); 
        }*/
		// si idTramite != null, buscar el tramite
		// si idTramite == null, buscar todos los tramites
		// por cada tramite
		// generar documento persona
		// generar documento operacion
		return docs;
	}

	private SolrField addFieldSolar(String pName, String pValue) {
		SolrField fielSolar = new SolrField();
		fielSolar.setName(pName);
		fielSolar.setValue(pValue);
		return fielSolar;
	}
	
	private void uploadFile(File file) {
		// create upload service client 
		// https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
		// use the FileUtils to get the actual file by uri 
		//File file = FileUtils.getFile(this, fileUri);
		// create RequestBody instance from file 
		RequestBody requestFile = RequestBody.create(MediaType.parse("application/xml"), file);
		// MultipartBody.Part is used to send also the actual file name 
		MultipartBody.Part body = MultipartBody.Part.createFormData("archivo", file.getName(), requestFile);
		// add another part within the multipart request 
		String descriptionString = "hello, this is description speaking";
		RequestBody description = RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString);
		// finally, execute the request 
		Call<ResponseBody> call = service.indexFile(description, body, 1000, true);
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				System.out.println("Finalizado con exito");
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {
				System.out.println("Upload error:" + t.getMessage());
			}
		});
	}
	
	public SolrResult searchUser(String query) {
		Call<SolrResult> call = service.search("catch-all:\"" + query + "\"~4");
		System.out.println("Query Generate " + "catch-all:\"" + query + "\"~4");
		try {
			Response<SolrResult> response = call.execute();
			return response.body();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
