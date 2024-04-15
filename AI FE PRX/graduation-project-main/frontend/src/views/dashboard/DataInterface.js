import React, { useState, useRef } from 'react'
import axios from 'axios'
import {proxyAddr}  from './ProxyAddr'

import {
  CButton,
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
  cilUser,
  cilMagnifyingGlass,
} from '@coreui/icons'


var arr_posts;
var search_keyword = "";

export const DrawChart = () => {
  const [keywdBtn, setKeywdBtn] = useState({disabledInput: false, disabledBtn: false, text: 'submit'})
  const [posts, setPosts] = useState([])
  const [x, getX] = useState([])
  const url = proxyAddr()

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
      .get(`${url}meme`, {
        params: {
          message: search_keyword
        },
        // message: search_keyword
      })
      .then((response) => {
        console.log('get response')
        console.log(response.data)
        arr_posts = response.data.result
        setPosts(arr_posts)
        // const flow = response.data.datasets
        // getFlow(flow)

        // const x_label = response.data.labels
        // getX(x_label)

        // arr_posts = response.data.posts;
        setKeywdBtn({disabledInput: false, disabledBtn: false, text: 'submit'})
    })
      .catch((error) => console.error(`keyword Search Error: ${error}`))
    }
    console.log('keywordBtn Clicked!')

  }

  // const postVisualize = () => {
  //   setPosts
  // }



  return (
      <CCard className="mb-4">
      <CButton color="warning" onClick={() => window.open('http://ark10806.iptime.org:6006')} className="d-grid gap-2">
        <CIcon icon={cilMagnifyingGlass} />3D Plot : Visualize Clusters
      </CButton>
        <CCardFooter>
            <hr/>
          <div>메시지를 입력 후 submit을 눌러주세요</div>
          <div>메시지는 번역 -&gt; multimodal 유사도 판단 -&gt; top-K 짤 반환의 과정을 거칩니다.</div>

            <CInputGroup>
              <CFormInput disabled={keywdBtn.disabledInput} onChange={keywordInputOnchangeHandler} placeholder="집 가고 싶다" aria-label="Recipient's username with two button addons"/>
              <CButton disabled={keywdBtn.disabledBtn} onClick={keywordBtnClickhandler} type="button" color="secondary" variant="outline">{toggleKeyword()}{keywdBtn.text}</CButton>
            </CInputGroup>
          {/* </CRow> */}

        </CCardFooter>
        <CCardBody>
          <CRow>
            <CCol sm={5}>
              <h4 id="traffic" className="card-title mb-0">
              </h4>
              <div className="small text-medium-emphasis">각 클러스터는 위의 3D Plotly 버튼을 통해 시각적으로 확인할 수 있습니다.</div>
            </CCol>
            <CCol sm={7} className="d-none d-md-block">
            </CCol>
          </CRow>

          <br/>
          {posts.map((item, index) => (
            <CRow key={index}>
              <br/>
              <hr/>
              <br/>
              <CCol key="0">
                  <CImage rounded thumbnail src={item.image_url} width='500vw' />
              </CCol>
              <CCol key="1">
                <div className="border-start border-start-4 border-start-danger py-1 px-3 mb-3">
                  {/* <div>{item.similarity}</div> */}
                </div>
                <div className="progress-group mb-4">
                  <div className="progress-group-header">
                    <CIcon className="me-2" icon={cilUser} size="lg" />
                    <span>Similarity</span>
                    <span className="ms-auto fw-semibold">{parseInt(item.similarity*100*2)}%</span>
                  </div>
                  <div className="progress-group-bars">
                    <CProgress thin color="success" value={parseInt(item.similarity*100*2)} />
                  </div>
                </div>
                <CButton color="#00aced" onClick={() => window.open(item.posturl)} className="float-end">
                  {/* <CIcon icon={cibTwitter} />see post */}
                </CButton>
              </CCol>
              <br/>
            </CRow>
          ))}

        </CCardBody>
      </CCard>
  )
}
