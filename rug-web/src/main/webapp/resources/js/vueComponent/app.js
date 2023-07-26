new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    data () {
        return {
            tab: 'subscribe',
            search: '',
            router:{
                post: {
                    tramites:'https://operaciones.rgm.gob.gt/'
                }
            },
            garantia: "",
            no_anio: "",
            item_anio: [],
            headers: [
                {
                    text: 'N\u00FAmero de Garant\u00EDa',
                    value: 'idGarantia',
                },
                {
                    text: 'Tr\u00E1mite',
                    value: 'descripcion',
                },
                {
                    text: 'Fecha',
                    value: 'fecha',
                },
                {
                    text: 'Valor',
                    value: 'precio',
                },
                {
                    text: 'Usuario',
                    value: 'nombrePersona',
                },
            ],
            desserts: [],
            snackbar: false,
            text: '',
            timeout: 2000,
            attrs: {
                class: 'mb-6',
                boilerplate: true,
                elevation: 2,
            },
            loading_data: false

        }
    },
    mounted(){

    },
    methods: {
        exportExcel(idPersona, url) {
            let yearData;
            if(this.no_anio != null){
                yearData = '2023';
            }else{
                yearData = this.no_anio.anio.trim();
            }
            // const Baseurl = url + '/rs/tramites/reporte-excel';
            axios.post(this.router.post.tramites + 'api/export-excel', {
                usuario: idPersona,
                anio: yearData
            }, { responseType: "blob"}).then(response => {
                var fileURL = window.URL.createObjectURL(new Blob([response.data]));
                var fileLink = document.createElement("a");
                fileLink.href = fileURL;
                let Filename = 'Reporte del a\u00f1o ' + yearData + '.xlsx'
                fileLink.setAttribute("download", Filename);
                document.body.appendChild(fileLink);
                fileLink.click();
                this.snackbar = true
                this.text = 'Espere a que se descargue el archivo'
            })
        },
        getAll(idPersona, url){
            this.getAnio(idPersona, url)
            this.getTramites(idPersona, url , '2022')
        },
        getAnio(idPersona, url){
            axios.post(url + '/rs/tramites/reporte-anio', {
                idUsuario:idPersona
            }).then(response => {
                this.item_anio = response.data
            })
        },
        update(idPersona, url){
            if(this.no_anio != null){
                this.getTramites(idPersona, url , this.no_anio.anio)
            }
        },
        getTramites(idPersona, url, anio){
            axios.post(this.router.post.tramites + 'api/reporte-tramites-filtro', {
            // axios.post(url + '/rs/tramites/reporte', {
                usuario:idPersona,
                anio: anio
            }).then(response => {
                this.loading_data =  false
                this.desserts = response.data
                this.loading_data = true

            })
        },
    }
})