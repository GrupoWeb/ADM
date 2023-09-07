/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package gt.gob.rgm.adm.rs;


import gt.gob.rgm.adm.annotation.SecuredResource;
import gt.gob.rgm.adm.domain.ResponseRs;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
/**
 * REST Web Service
 *
 * @author talo4
 */
@Path("/fileupload")
@RequestScoped
public class FileuploadResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of FileuploadResource
     */
    private BufferedImage resizeImage(InputStream inputImage, int newWidth, int newHeight) throws IOException {
    BufferedImage originalImage = ImageIO.read(inputImage);
    BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics2D = resizedImage.createGraphics();
    graphics2D.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
    graphics2D.dispose();
    return resizedImage;
}
    public FileuploadResource() {
        System.out.println("PRINCIPAL");
    }
    @POST
    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    @Produces(MediaType.APPLICATION_JSON)
    @SecuredResource
    public ResponseRs addAdjunto(@FormDataParam("adjunto") InputStream inputStream,
                                                       @FormDataParam("adjunto") FormDataContentDisposition formDataContentDisposition,							   
                                                       @Context HttpServletRequest request) {
            ResponseRs response = new ResponseRs();
try {
    // ...
    BufferedImage resizedImage = resizeImage(inputStream, 250, 64);

    String path = request.getServletContext().getRealPath("") + File.separator + "assets"+ File.separator + "img"+ File.separator +"logo_RGM.jpg";
    String path2 = request.getServletContext().getRealPath("") + File.separator + "assets"+ File.separator + "img"+ File.separator +"logo2v1.jpg";
    

    try (OutputStream outputStream = new FileOutputStream(path)) {
    
        ImageIO.write(resizedImage, "jpg", outputStream);
    }
     try (OutputStream outputStream = new FileOutputStream(path2)) {
    
        ImageIO.write(resizedImage, "jpg", outputStream);
    }
   response.setTotal(1L);
   response.setData(path);
    System.out.println("RESPONDE");
    System.out.println(response);
   return response;
    	
} catch (IOException e) {
   response.setTotal(0L);
   
   return response;
}
           /* try {
                    //String path = request.getServletContext().getRealPath("") +"/"+ formDataContentDisposition.getFileName();
                    String path = request.getServletContext().getRealPath("") +File.separator+"logo_RGM.jpg";                    
                    System.out.println("URL:" + path);
                    String relativePath = "/assets/img/"+formDataContentDisposition.getFileName();
                    try (OutputStream outputStream = new FileOutputStream(path)) {
                                    System.out.println("outputStream: "+outputStream);
                                    int read;
                                    byte[] bytes = new byte[1024];
                                    while ((read = inputStream.read(bytes)) != -1) {
                                        outputStream.write(bytes, 0, read);
                                    }
                                    outputStream.flush();
                                }



                    return Response.ok("Archivo subido exitosamente JPG")
                                    .header("Access-Control-Allow-Orign", "*")
                                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD")
                                    .build();
            } catch(Exception e) {
                    Response response = Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage())
                                    .header("Access-Control-Allow-Orign", "*")
                                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD")
                                    .build();
                    throw new WebApplicationException(response);
            }	*/	
    }
    /**
     * Retrieves representation of an instance of gt.gob.rgm.adm.rs.FileuploadResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        System.out.println("GET");
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of FileuploadResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
