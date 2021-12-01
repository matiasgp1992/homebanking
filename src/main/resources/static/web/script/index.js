const app= Vue.createApp({
    data(){
        return{
            email: "",
            password: "",
            mensj: false,
            registro: false,
            firstName: "",
            lastName: "",
           
            mensaje: false
        }
    },
    created(){
        
    },
    methods:{
        login(){
            axios.post('/api/login',`email=${this.email}&password=${this.password}`,
            {headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response => {
                if(this.email.includes("@admin.com")){
                    window.location.assign("/manager.html")
                } else {
                    window.location.assign("/web/accounts.html")
                }
                
                this.email=""
                this.password=""
            })
                
            .catch(e => {
                this.mensj= true;

            });
            
        },
        logOut(){
            axios.post('/api/logout')
            .then(response => window.location.replace("/web/index.html"))
            .catch(e => {
                console.log(e);
            });
        },
        registrar(){
            this.email=""
            this.password=""
            this.registro= true
        },
        crear(e){
            e.preventDefault();
            axios.post('/api/clients', `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}&type=AHORRO`,
            {headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(res => {
                this.login()
                axios.post('/api/clients', { headers: { 'content-type': 'application/x-www-form-urlencoded' }})
            })
            .catch(e => {
                this.mensaje= true;
            })
            
        }
    },
    computed:{
        
    }
});

app.mount("#app");