import React, { useState, useRef } from 'react'
import axios from 'axios'
import {proxyAddr}  from './ProxyAddr'
// import { proxyAddr } from './ProxyAddr'

import {
  CButton,
  CButtonGroup,
  CCard,
  CCardBody,
  CCardFooter,
  CProgress,
  CCol,
  CRow,
  CImage,
  CSpinner,
  CFormInput,
  CInputGroup,
} from '@coreui/react'
axios.defaults.withCredentials = true;


import CIcon from '@coreui/icons-react'
import {
  cibTwitter,
  cilCloudDownload,
  cilUser,
  cilMagnifyingGlass,
} from '@coreui/icons'

import { Line, getElementAtEvent } from "react-chartjs-2";
import { Chart, registerables } from 'chart.js';
Chart.register(...registerables);

var btnColors = [
  false, false, false, false, false, false, false, false, false, false, false
]


//! daily, weekly, montly, year로 다양화 필요
const categoryPanel = [
  {title: 'politics'},
  {title: 'social'},
  {title: 'economy'},
  {title: 'fashion'},
  {title: 'entertain'},
  {title: 'sports'},
  {title: 'foods'},
  {title: 'animals'},
  {title: 'selfies'},
  {title: 'memes'},
  {title: 'cartoons'},
  {title: 'movies'},
  {title: 'games'},
  {title: 'landscapes'},
]


var arr_posts;
var search_keyword = "";

export const DrawChart = () => {
  const [keywdBtn, setKeywdBtn] = useState({disabledInput: false, disabledBtn: false, text: 'submit'})
  const [posts, setPosts] = useState([])
  const [flows, getFlow] = useState([])
  const [x, getX] = useState([])
  const [btnColor, setColor] = useState(btnColors)
  const url = proxyAddr()
  const start_date = new Date('2021-01-01')
  const end_date = new Date('2022-02-01')
  const chartRef = useRef();

  const chartClickHandler = (event) => {
    var elmt = getElementAtEvent(chartRef.current, event);
    if (elmt[0]){
      const dataset_idx = elmt[0].datasetIndex
      const data_idx = elmt[0].index
      console.log(arr_posts[dataset_idx][data_idx])
      setPosts(arr_posts[dataset_idx][data_idx])
    }
  }

  const toggleKeyword = () => {
    if (keywdBtn.disabledInput){
      return (
        <CSpinner component="span" size="sm" aria-hidden="true"/>
      )
    }
    else{
      return (
        <></>
      )
    }
  }

  const keywordInputOnchangeHandler = (event) => {
    search_keyword = event.target.value;
    console.log(`Input changed: ${search_keyword}`)
  }

  const keywordBtnClickhandler = (event) => {
    if (search_keyword !== ""){
      setKeywdBtn({disabledInput: true, disabledBtn: true, text: 'Loading..'})
      console.log(search_keyword);
      axios
      .get(`${url}keyword-search/get`, {
        params: {
          start_date: start_date,
          end_date: end_date,
          keyword: search_keyword
        },
      })
      .then((response) => {
        console.log('get response')
        const flow = response.data.datasets
        getFlow(flow)

        const x_label = response.data.labels
        getX(x_label)

        arr_posts = response.data.posts;
        setKeywdBtn({disabledInput: false, disabledBtn: false, text: 'submit'})
    })
      .catch((error) => console.error(`keyword Search Error: ${error}`))
    }
    console.log('keywordBtn Clicked!')

  }



  const categoryClickHandler = (event) => {
    const btnId = event.target.getAttribute('id')
    // btnColors[btnId] = !btnColors[btnId]
    btnColors = [ false, false, false, false, false, false, false, false, false, false, false ]
    btnColors[btnId] = true
    setColor(btnColors)
    setPosts([])
    const selected_categories = [];
    for (var i=0; i<btnColors.length; i++){
      if (btnColors[i])
        selected_categories.push(i+1);
    }

    axios
      .get(`${url}categorical-chart/get`, {
        params: {
          start_date: start_date,
          end_date: end_date,
          selected_categories: selected_categories,
        },
      })
      .then((response) => {
        const flow = response.data.datasets
        getFlow(flow)

        const x_label = response.data.labels
        getX(x_label)

        arr_posts = response.data.posts;
      })
      .catch((error) => console.error(`Error: ${error}`))
  }



  return (
      <CCard className="mb-4">
      {/* <CButton color="warning" onClick={() => window.open('http://localhost:6006')} className="d-grid gap-2"> */}
      <CButton color="warning" onClick={() => window.open('http://ark10806.iptime.org:6006')} className="d-grid gap-2">
        <CIcon icon={cilMagnifyingGlass} />3D Plot : Visualize Clusters
      </CButton>
        <CCardFooter>
          <CRow xs={{ cols: 2 }} md={{ cols: 5 }} className="text-center">
            {categoryPanel.map((item, index) => (
                <CCol className="mb-sm-2 mb-0" key={index}>
                <CButton id={index} onClick={categoryClickHandler} className="text-medium-emphasis" shape='rounded-pill' color={btnColor[index]===true ? 'success' : '#9da5b1'} style={{ width: '120px' }}>{item.title}</CButton>
                </CCol>
            ))}
            </CRow>
            <hr/>
          {/* <CRow xs={{ cols: 2 }} md={{ cols: 2 }} className="text-center"> */}
          <div>키워드는 짧은 단어 "Trump" 대신 "Donald Trump the president of United States" 같이 문장으로 제공할 때 좋은 검색 성능을 보입니다.  </div>

            <CInputGroup>
              <CFormInput disabled={keywdBtn.disabledInput} onChange={keywordInputOnchangeHandler} placeholder="Search twit by your own keyword (ex. vaccination for covid-19.)" aria-label="Recipient's username with two button addons"/>
              <CButton disabled={keywdBtn.disabledBtn} onClick={keywordBtnClickhandler} type="button" color="secondary" variant="outline">{toggleKeyword()}{keywdBtn.text}</CButton>
            </CInputGroup>
          {/* </CRow> */}

        </CCardFooter>
        <CCardBody>
          <CRow>
            <CCol sm={5}>
              <h4 id="traffic" className="card-title mb-0">
                Trends
              </h4>
              <div className="small text-medium-emphasis">January - December 2021</div>
              <div className="small text-medium-emphasis">그래프의 각 지점을 클릭하여 관련 포스트들을 확인하실 수 있습니다.</div>
              <div className="small text-medium-emphasis">각 클러스터의 범례(legend)를 클릭하여 일부 클러스터를 보이지 않게 할 수 있습니다</div>
              <div className="small text-medium-emphasis">각 클러스터는 위의 3D Plotly 버튼을 통해 시각적으로 확인할 수 있습니다.</div>
            </CCol>
            <CCol sm={7} className="d-none d-md-block">
            </CCol>
          </CRow>

          <Line
            onClick={chartClickHandler}
            ref={chartRef}
            id="myChart"
            data={{
              labels: x,
              datasets: flows.map((value) => ({
                label: value.label,
                data: value.data,
                fill: true,
                backgroundColor: "rgba(75,192,192,0.2)",
                borderColor: "rgba(75,192,192,1)"
              })),
          }}
          options= {{
            responsive: true,
          }}
          ></Line>
          <br/>
          {posts.map((item, index) => (
            <CRow key={index}>
              <br/>
              <hr/>
              <br/>
              <CCol key="0">
                  <CImage rounded thumbnail src={item.image} width='500vw' />
                  {/* <CImage rounded thumbnail src={item.image} width='256px' height='512hv' /> */}
              </CCol>
              <CCol key="1">
                <div className="border-start border-start-4 border-start-danger py-1 px-3 mb-3">
                  <div>{item.text}</div>
                </div>
                <div className="progress-group mb-4">
                  <div className="progress-group-header">
                    <CIcon className="me-2" icon={cilUser} size="lg" />
                    <span>Cluster Similarity</span>
                    <span className="ms-auto fw-semibold">{parseInt(item.similarity*100)}%</span>
                  </div>
                  <div className="progress-group-bars">
                    <CProgress thin color="success" value={parseInt(item.similarity*100)} />
                  </div>
                </div>
                <CButton color="#00aced" onClick={() => window.open(item.posturl)} className="float-end">
                  <CIcon icon={cibTwitter} />see post
                </CButton>
              </CCol>
              <br/>
            </CRow>
          ))}

        </CCardBody>
      </CCard>
  )
}
