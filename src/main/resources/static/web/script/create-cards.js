const app= Vue.createApp({
    data(){
        return{
            cardType: "",
            cardColor: "",
            mensajeTarjetas: false
        }
    },
    created(){

    },
    methods: {
        logOut(){
            axios.post('/api/logout')
            .then(response => window.location="/web/index.html")
            .catch(e => {
                console.log(e);
            });
        },
        crearTarjeta(){
            axios.post('/api/clients/current/cards', `type=${this.cardType}&color=${this.cardColor}`, 
                {headers: {'content-type': 'application/x-www-form-urlencoded'}})
                .then(res => window.location="/web/cards.html")
                .catch(e => this.mensajeTarjetas=!this.mensajeTarjetas)
        },
        cerrarMensaje(){
            this.mensajeTarjetas = !this.mensajeTarjetas;
        },
        confirmar() {
            if (this.cardType == "" || this.cardColor == "") {
                return true
            }
            return false
        }
    },
    computed: {
        
    }
});

app.mount("#app");