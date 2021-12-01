const app= Vue.createApp({
    data(){
        return{
            accounts: [],
            transacciones: [],

            fromDate: "",
            toDate: ""
        }
    },
    created(){
        this.loadData()
        
    },
    methods:{
        logOut(){
            axios.post('/api/logout')
            .then(response => window.location="/web/index.html")
            .catch(e => {
                console.log(e);
            });
        },
        loadData(){
            const urlParams = new URLSearchParams(window.location.search);
            const myParam = urlParams.get('id');
       
            axios.get('/api/accounts/' + myParam)
            .then(response => {
                this.accounts = response.data;
                this.transacciones = response.data.transactions;
                this.ordenar()
            })
            .catch(e => {
                console.log(e);
            })
        },
        ordenar(){
            this.transacciones= this.transacciones.sort((a, b)=>{
                if (a.id < b.id) {
                    return 1;
                }
                if (a.id > b.id) {
                    return -1;
                }
                return 0;
            });
            return this.transacciones;
        },
        formateoFecha(date) {
            return new Date(date).toLocaleDateString('en-gb');
        },
        getTransactions() {
            if (this.fromDate == "") {
                return swal("Necesitas establecer una fecha de inicio")
            }
            if (this.toDate == "") {
                this.toDate = new Date(Date.now()).toLocaleDateString('en-gb').split("/").reverse().join("-")
            }
            axios.post('/api/transactionFilter', {
                fromDate: this.fromDate.replace('/', '-'),
                untilDate: this.toDate.replace('/', '-'),
                accountNumber: this.accounts.number
            })
                .then(res => this.transacciones = res.data)
                .then(() => {
                    if (this.transacciones == "") {
                        swal('No hay transacciones en esas fechas')
                    }
                })
                .catch(err => console.log(err))
        },
        getTransactionsToPDF(){
            if (this.fromDate == "") {
                return swal("Necesitas establecer una fecha de inicio")
            }
            if (this.toDate == "") {
                this.toDate = new Date(Date.now()).toLocaleDateString('en-gb').split("/").reverse().join("-")
            }
            axios({
                url: '/api/transaction/export/pdf',
                method: 'POST',
                responseType: 'blob',
                data: {
                    fromDate: this.fromDate.replace('/', '-'),
                    untilDate: this.toDate.replace('/', '-'),
                    accountNumber: this.accounts.number
                }
            })
                .then((response) => {
                    const url = window.URL.createObjectURL(new Blob([response.data]));
                    const link = document.createElement('a');
                    link.href = url;
                    link.setAttribute('download', 'Transactions_from_' + this.fromDate + '_to_' + this.toDate + '.pdf');
                    document.body.appendChild(link);
                    link.click();
                })
                .catch(err => swal(err))
        }
    },
    computed:{
        
    }
});

app.mount("#app");