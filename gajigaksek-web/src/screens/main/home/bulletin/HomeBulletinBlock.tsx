import React from "react";
import "./HomeBulletinBlock.css";
import Cookies from "universal-cookie";

const cookies = new Cookies();

interface HomeBulletinBlockProps {
  new: boolean;
  title: string;
  date: string;
  noticeId: number;
}

export default class HomeBulletinBlock extends React.Component<
  HomeBulletinBlockProps,
  Record<string, never>
> {
  render() {
    return (
      <div className="home-bulletin-block">
        <div className="home-bulletin-title-wrapper">
          {this.props.new && (
            <div className="home-bulletin-new pretendard">NEW</div>
          )}
          <a
            onClick={() => {
              cookies.set("noticeId", this.props.noticeId);
              window.location.href = "/main/bulletin";
            }}
            className="home-bulletin-title pretendard"
          >
            {this.props.title}
          </a>
        </div>
        <div className="home-bulletin-date pretendard">{this.props.date}</div>
        <div className="home-bulletin-separator"></div>
      </div>
    );
  }
}
