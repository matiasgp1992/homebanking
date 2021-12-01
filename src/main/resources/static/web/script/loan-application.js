const app= Vue.createApp({
    data(){
        return{
            client:[],
            accounts: [],
            loans:"",
            loanName: "",
            loanAccount: "",
            loanAmount: "",
            loanPayment: "",
            mensaje:""
        }
    },
    created(){
        axios.get('/api/clients/current')
            .then(response => {
                this.client = response.data;
                this.accounts = response.data.accounts;
            })
            .catch(e => {
                console.log(e);
            })

        axios.get('/api/loans')
        .then(res => {
            this.loans = res.data
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
        balance(){
            if (this.loanAccount != "") {
                return this.accounts.filter(e => e.number == this.loanAccount)[0].balance.toFixed(2);
            }
        },
        maxAmount() {
            if (this.loanName != "") {
                return this.loans.filter(e => e.name == this.loanName)[0].maxAmount.toFixed(2);
            }
        },
        cuotas() {
            if (this.loanName != "") {
                return this.loans.filter(e => e.name == this.loanName)[0].payments;
            }
        },
        cuotasAPagar() {
            if (this.loanPayment == "" || this.loanAmount == "") {
                return 0;
            }
            if (this.loanName == "Hipotecario") {
                return (this.loanAmount * 1.20 / this.loanPayment).toFixed(2);
            }
            if (this.loanName == "Automotriz") {
                return (this.loanAmount * 1.20 / this.loanPayment).toFixed(2);
            }
            if (this.loanName == "Personal") {
                return (this.loanAmount * 1.20 / this.loanPayment).toFixed(2);
            }
        },
        confirmar() {
            if (this.loanAccount == "" || this.loanName == "" || this.loanAmount == 0 || this.loanPayment == "") {
                return true
            }
            return false
        },
        resetForm(){
            this.loanAccount = "";
            this.loanAmount = "";
            this.loanPayment = "";
            this.loanName = "";
        },
        aplication() {
            axios.post('/api/loans', {account: this.loanAccount, name: this.loanName, amount: this.loanAmount, payment: this.loanPayment})
                .then(res => {
                    this.mensaje= res.data
                })
                .catch(e => {
                    this.mensaje= e.response.data
                })
                .then(setTimeout(function(){ window.location="/web/accounts.html"; }, 3000))
            this.resetForm()
        }
    },
    computed:{

    }
});

app.mount("#app");