new Vue({
    el: '#app',
    data () {
        return {
            router:{
                post: {
                    tramites:'https://api.rgm.gob.gt/'
                }
            },

        }
    },
    updated(){
        console.log("Cargado")
    },
    methods: {
        cambioPermiso(){
            console.log("Desde vue")
        }
    }
})