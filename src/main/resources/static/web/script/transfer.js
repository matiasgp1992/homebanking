const app= Vue.createApp({
    data (){
        return{
            client: [],
            fromAccount: "",
            owner: "",
            toAccount: "",
            transferAmount: "",
            transferDesc: "",
            disabled: false,
            mensaje: ""
        }
    },
    created(){
        axios.get('/api/clients/current')
            .then(response => {
                this.client = response.data;
            })
            .catch(e => {
                console.log(e);
            })
    },
    methods:{
        logOut(){
            axios.post('/api/logout')
            .then(response => window.location="/web/index.html")
            .catch(e => {
                console.log(e);
            });
        },
        cuentaDisponible(){
            if(this.client != "") {
                return this.client.accounts.filter(acc => acc.number != this.fromAccount)
            }
        },
        resetToAccount() {
            this.toAccount = ""
        },
        resetTransfer(){
            this.fromAccount= ""
            this.toAccount= ""
            this.transferAmount= ""
            this.transferDesc= ""
            this.destinationAccount= ""
            this.owner= ""
        },
        transferPost(){
            this.disabled=true

            axios.post('/api/transactions', "amount=" + this.transferAmount + "&description=" + this.transferDesc + "&fromNumber=" + this.fromAccount + "&toNumber=" + this.toAccount, 
                { headers: {'content-type': 'application/x-www-form-urlencoded'}})
                .then(res => {
                    this.mensaje= res.data
                    setTimeout(function(){ window.location="/web/accounts.html"; }, 3000)
                })
                .catch(e => {
                    this.mensaje= e.response.data
                })

            this.resetTransfer()
        }
    },
    computed:{
        btnDes() {
            if(this.fromAccount!="" && this.toAccount!="" && this.transferAmount!="" && this.transferDesc!=""){
               return false
            }
            return true
        },
    }
});

app.mount("#app");