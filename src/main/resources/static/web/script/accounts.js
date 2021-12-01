const app= Vue.createApp({
    data(){
        return {
            accounts: [],
            loans: [],
            mensaje: false,

            deleteAccNumber: "",
            mensaje:"",

            accType:""
        }
    },
    created(){
        this.loadData()
    },
    methods: {
        loadData(){
            axios.get('/api/clients/current')
            .then(response => {
                this.accounts = response.data.accounts;
                this.ordenar(this.accounts);
                this.loans= response.data.loans;
                this.ordenar(this.loans);
            })
            .catch(e => {
                console.log(e);
            })
        },
        cambiarPag(accountId){
            return "./account.html?id=" + accountId
        },
        ordenar(accounts){
            accounts.sort((a, b)=>{
                if (a.id < b.id) {
                    return -1;
                }
                if (a.id > b.id) {
                    return 1;
                }
                return 0;
            });
            return accounts;
        },
        logOut(){
            axios.post('/api/logout')
            .then(response => window.location="/web/index.html")
            .catch(e => {
                console.log(e);
            });
        },
        createAccount(){  
            axios.post("/api/clients/current/accounts","type=" + this.accType, {headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(res => {
                location.reload()
            })
            .catch(e => {
                this.mensaje= !this.mensaje;
                console.log(e);
            })
        },
        cerrarModal(){
            this.mensaje= !this.mensaje;
        },
        selecAcc(number) {
            this.deleteAccNumber = number
        },
        deleteAccount(){
            axios.delete('/api/clients/current/accounts/' + this.deleteAccNumber, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(res => {
                    this.mensaje= res.data
                    setTimeout(function(){ location.reload(); }, 2000)
                })
                
                .catch(err => {
                    this.mensaje = err.response.data
                })
        },
        confirmar() {
            if (this.accType == "") {
                return true
            }
            return false
        }
    },
    computed: {
        
    }
});

app.mount("#app");