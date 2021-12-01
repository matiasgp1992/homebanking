const app = Vue.createApp({
    data(){
        return {
            clients: [],
            clientsCrudo: [],
            formElements: ["", "", ""],


            isMenu: false,
            loanName: "",
            maxAmount: 0,
            paymentQuantity: 0,
            payments: [],
            payment: '',
            interest: 0,

            mensaje: ""
        }
    },
    created(){
        this.loadData();
    },
    methods: {
        logOut(){
                    axios.post('/api/logout')
                    .then(response => window.location="/web/index.html")
                    .catch(e => {
                        console.log(e);
                    });
        },
        loadData(){
            axios.get('/rest/clients')
            .then(response => {
                this.clientsCrudo= response.data;
                this.clients = response.data._embedded.clients;
            })
            .catch(e => {
                console.log(e);
            })
        },
        addClient(){
            let cliente={
                firstName: this.formElements[0],
                lastName: this.formElements[1],
                email: this.formElements[2]
            }
            this.postClient(cliente)
        },
        postClient(cliente){
            axios.post('/rest/clients', cliente).then(response => {
                this.formElements[0]="";
                this.formElements[1]="";
                this.formElements[2]="";
                this.loadData();
            }).catch(e =>{
                console.log(e)
            })
        },
        deleteClient(e){
            axios.delete(e.target.value).then(response=>{
                this.loadData();
            })
        },



        addPayment() {
            if (this.payment < 1) {
                return swal("Wrong payment")
            }
            parseInt(this.payment)
            this.payments.push(parseInt(this.payment))
            this.payment = ''
        },
        reset() {
            this.loanName = ""
            this.maxAmount = 0
            this.paymentQuantity = 0
            this.payments = []
            this.payment = ''
            this.interest = 0
        },
        newLoan() {
            axios.post("/api/admin/loan", { name: this.loanName, maxAmount: parseInt(this.maxAmount), payments: this.payments, interest: parseInt(this.interest) })
                .then(res => swal('Loan created succefully'))
                .then(() => this.reset)
                .catch(() => swal('Incorrect data'))
        }
    
    },
    computed: {
        habilitar(){
            if(this.formElements.every(element=> element !== "")){
                return false
            } else {
                return true
            }
        },

        paymentsSorted() {
            return this.payments.sort((a, b) => a - b)
        }
    }
})

app.mount("#app")