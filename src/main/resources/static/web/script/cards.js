const app= Vue.createApp({
    data(){
        return{
            cards:[],
            credit:[],
            debit:[],
            mensaje: false,

            deleteCardNumber: "",
            deleteCardId: "",
            deleteMessage: "",

        }
    },
    created(){
        this.loadData();
    },
    methods: {
        loadData(){
            axios.get('/api/clients/current')
            .then(response => {
                this.cards = response.data.cards;
                this.ordenar();
                this.credit = response.data.cards.filter(card => card.type == 'CREDIT');
                this.debit = response.data.cards.filter(card => card.type == 'DEBIT');
            })
            .catch(e => {
                console.log(e);
            })
        },
        ordenar(){
            this.cards= this.cards.sort((a, b)=>{
                if (a.id < b.id) {
                    return 1;
                }
                if (a.id > b.id) {
                    return -1;
                }
                return 0;
            });
            return this.cards;
        },
        logOut(){
            axios.post('/api/logout')
            .then(response => window.location="/web/index.html")
            .catch(e => {
                console.log(e);
            });
        },
        irCrearTarjeta(){
            if(this.cards.length == 6){
                this.mensaje= !this.mensaje;
            } else{
                window.location="/web/create-cards.html";
            }
        },
        cerrarMensaje(){
            this.mensaje = !this.mensaje;
        },
        deleteCardConfirm() {
            axios.delete('/api/clients/current/cards/delete/' + this.deleteCardId)
                .then(res => this.deleteMessage= res.data)
                .then(res => {
                    setTimeout(function(){ location.reload(); }, 2000)
                })
                .catch(err => {  
                    this.deleteMessage = err.response.data
                    setTimeout(function(){ location.reload(); }, 2000)
                })
        },
        expiredCard(tarjeta) {
            let dateNow = new Date()
            let expirationDate = new Date(tarjeta.thruDate)
            return expirationDate < dateNow
        },
        deleteCard(id, number) {
            this.deleteCardId = id
            this.deleteCardNumber = number
        }
    },
    computed: {
        
    }
});

app.mount("#app");