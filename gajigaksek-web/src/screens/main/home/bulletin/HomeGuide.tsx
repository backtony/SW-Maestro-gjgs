import React from "react";
import "./HomeGuide.css";

export default class HomeGuide extends React.Component {
  render() {
    return (
      <div className="home-guide-container">
        <div className="home-guide-upper">
          <div className="home-guide-title pretendard">
            작가페이지 사용 가이드
          </div>
          <div className="home-guide-button-container">
            <button className="home-guide-button pretendard">
              <i className="fas fa-headset fa home-guide-icon"></i>
              1:1 실시간 상담
            </button>
            <button className="home-guide-button pretendard">
              <i className="fas fa-book fa home-guide-icon"></i>
              이용가이드 보기
            </button>
            <button className="home-guide-button pretendard">
              <i className="fas fa-play-circle fa home-guide-icon"></i>
              동영상가이드 보기
            </button>
          </div>
        </div>
        <div className="home-guide-down">
          <div className="home-guide-text pretendard">고객센터: 1234-5678</div>
          <div className="home-guide-text pretendard">운영시간 10시~18시</div>
        </div>
      </div>
    );
  }
}
