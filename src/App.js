import logo from './logo.svg';
import './App.css';
import { CartesianGrid, Legend, Line, LineChart, Tooltip, XAxis, YAxis } from 'recharts';
import { scalePow, scaleLog } from 'd3-scale';
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';
import {Component}from "react"
const options = [
  'TÜM ŞEHİRLER',"Adana", "Adıyaman", "Afyon", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin", "Aydın", "Balıkesir", "Bilecik", 
  "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", 
  "Çanakkale", "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Edirne", "Elazığ", "Erzincan", "Erzurum", "Eskişehir", "Gaziantep", "Giresun", 
  "Gümüşhane", "Hakkari", "Hatay", "Isparta", "İçel (Mersin)", "İstanbul", "İzmir", "Kars", "Kastamonu", "Kayseri", "Kırklareli", "Kırşehir",
  "Kocaeli", "Konya", "Kütahya", "Malatya", "Manisa", "K.maraş", "Mardin", "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu", "Rize", "Sakarya", 
  "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ", "Tokat", "Trabzon", "Tunceli", "Şanlıurfa", "Uşak", "Van", "Yozgat", "Zonguldak", "Aksaray", 
  "Bayburt", "Karaman", "Kırıkkale", "Batman", "Şırnak", "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük", "Kilis", "Osmaniye", "Düzce"
];
const defaultOption = options[0];

class App extends Component {

  constructor(){
    super()
    this.state={
    data:[],value:"",
    selectedCity:"TÜM ŞEHİRLER"
    }
    this.getData("TÜM ŞEHİRLER")
  }
  shouldComponentUpdate(nextProps,nextState){
    //WHEN SELECTİON CHANGE İT FETCH NEW DATA FROM SPRİNG-BOOT
    if(this.state.selectedCity !== nextState.selectedCity){
      this.getData(nextState.selectedCity)
    }
    return true
  }

  //get data from spring boot back-end
  getData=(city)=>{
    const requestOptions = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body:city
  };
  // SEND A REQUEST TO BACKEND AND GET DATA
  fetch('http://localhost:8080/api/caseSummary', requestOptions)
      .then(response => response.json())
      .then(data => {
        console.log("data ",data)
        //SET NEW DATA
        this.setState({data:data})
      });
  }
  _onSelect=(data)=>{
    //SET NEW CİTY WHEN DROP DOWN CHANGED
    this.setState({selectedCity:data.value})
  }
  
  //change method of input text
  handleChange=(event)=> {
    
    this.setState({value: event.target.value});
  }

  handleSubmit=(event) =>{
    // GET DATA FROM SPRİNG-BOOT WHEN CİTY CHANGE
        const requestOptions = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body:this.state.value
  };
  fetch('http://localhost:8080/api/insertCase', requestOptions)
      .then(response => response.json())
      .then(data => {
        if(!data.id){
            alert("HABER İÇERİĞİ DEĞERLENDİRİLEMEDİ")
        }else{
          this.getData(this.state.selectedCity)
          alert("DEĞERLER EKLENDİ")
        }
      });
    event.preventDefault();
  }
   
  render(){

    let data = this.state.data
      return (
      <div className="App">       
         <Dropdown options={options} onChange={this._onSelect} value={defaultOption}
          placeholder="TÜM ŞEHİRLER" />

          <form onSubmit={this.handleSubmit}>
        <label>
          Haber gir:
          <input type="text" value={this.state.value} onChange={this.handleChange} />
        </label>
        <input type="submit" value="Submit" />
      </form>

        <header className="App-header">

          <div> {this.state.selectedCity ?this.state.selectedCity:" TÜM ŞEHİRLER"}</div>
        <div className='line-chart-wrapper'>
            <LineChart
              width={1200}
              height={400}
              data={data}
              margin={{ top: 20, right: 20, bottom: 20, left: 20 }}
              syncId="test"
            >
              <CartesianGrid stroke='#f5f5f5' verticalFill={['rgba(0, 0, 0, 0.2)', 'rgba(255, 255, 255, 0.3)']} horizontalFill={['#ccc', '#fff']} />
              <Legend />
              <XAxis  dataKey="date" axisLine={{ stroke: 'black' }} />
              <YAxis />
              <Line type='monotone' dataKey='toplamTaburcu' stroke='green' />
              <Line type='monotone' dataKey='toplamVefat' stroke='red' />
              <Line type='monotone' dataKey='toplamVaka' stroke='yellow' />

            </LineChart>
          </div>
        </header>
      </div>
    );
  }

}

export default App;
