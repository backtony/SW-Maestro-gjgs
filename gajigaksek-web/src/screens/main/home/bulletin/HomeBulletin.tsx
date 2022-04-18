import React from "react";
import NoticeController from "../../../../services/controllers/NoticeController";
import "./HomeBulletin.css";
import HomeBulletinBlock from "./HomeBulletinBlock";
import HomeGuide from "./HomeGuide";
import HomeNumberButton from "./HomeNumberButton";

export interface NoticeInfo {
  title: string;
  text: string;
  createdDate: string;
  noticeId: number;
}

interface HomeBulletinStates {
  page: number;
  notices: NoticeInfo[];
  totalPage: number;
  naviNumbers: number[];
}

const noticeList: NoticeInfo[] = [
  {
    title: "[프로모션] 11번가와 함께하는 프로모션",
    text: "hello world",
    createdDate: "2021년 8월 26일",
    noticeId: 0,
  },
  {
    title: "[프로모션] 11번가와 함께하는 프로모션",
    text: "hello world",
    createdDate: "2021년 8월 26일",
    noticeId: 1,
  },
  {
    title: "[프로모션] 11번가와 함께하는 프로모션",
    text: "hello world",
    createdDate: "2021년 8월 26일",
    noticeId: 2,
  },
  {
    title: "[프로모션] 11번가와 함께하는 프로모션",
    text: "hello world",
    createdDate: "2021년 8월 26일",
    noticeId: 3,
  },
];

export default class HomeBulletin extends React.Component<
  Record<string, never>,
  HomeBulletinStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      page: 0,
      totalPage: 5,
      notices: noticeList,
      naviNumbers: [0, 1, 2, 3, 4],
    };
    this.getNotices(0);
  }

  private async getNotices(page: number) {
    try {
      const res = await NoticeController.getNotices(page);
      console.log("res:", res);
      this.setState({
        notices: res?.data.content,
        totalPage: res?.data.totalPages,
      });
    } catch (e) {}
  }

  private refreshNaviNumbers(page: number) {
    let startNum = 0;
    for (; startNum <= page; startNum += 5) {}

    startNum -= 5;

    const ret = [];

    for (
      let num = startNum;
      num < startNum + 5 && num < this.state.totalPage;
      num++
    ) {
      ret.push(num);
    }

    this.setState({ naviNumbers: ret });
  }

  private dateParse(rawDate: string) {
    const date = new Date(rawDate);

    const YY = date.getFullYear();
    const MM = date.getMonth() + 1;
    const DD = date.getDate();
    return `${YY}년 ${MM}월 ${DD}일`;
  }

  private isNew(rawDate: string) {
    const threeDayAgo = new Date();
    const noticeDate = new Date(rawDate);
    threeDayAgo.setDate(threeDayAgo.getDate() - 3);
    return noticeDate > threeDayAgo;
  }

  render() {
    return (
      <div className="home-bulletin-container">
        <div className="home-bulletin-wrapper">
          <div className="home-bulletin-title pretendard">공지사항</div>
          <div className="home-bulletin-list">
            {this.state.notices.map((notice: NoticeInfo) => {
              const parsedDate = this.dateParse(notice.createdDate);
              return (
                <HomeBulletinBlock
                  new={this.isNew(notice.createdDate)}
                  title={notice.title}
                  date={parsedDate}
                  noticeId={notice.noticeId}
                />
              );
            })}
          </div>
          <div className="home-page-navigation">
            <button
              className="home-page-text pretendard home-page-button"
              onClick={() =>
                this.setState({ page: 0 }, () => {
                  this.getNotices(0);
                  this.refreshNaviNumbers(0);
                })
              }
            >
              처음
            </button>
            <button
              className="home-page-text pretendard home-page-button"
              onClick={() => {
                if (this.state.page > 0)
                  this.setState({ page: this.state.page - 1 }, () => {
                    this.getNotices(this.state.page);
                    this.refreshNaviNumbers(this.state.page);
                  });
              }}
            >
              <i className="fas fa-chevron-left fa"></i>
            </button>
            {this.state.naviNumbers.map((num) => (
              <HomeNumberButton
                page={num}
                on={this.state.page === num}
                setPage={(page: number) =>
                  this.setState({ page }, () => {
                    this.getNotices(this.state.page);
                    this.refreshNaviNumbers(this.state.page);
                  })
                }
              />
            ))}

            <button
              className="home-page-text pretendard home-page-button"
              onClick={() => {
                if (this.state.page < this.state.totalPage)
                  this.setState({ page: this.state.page + 1 }, () => {
                    this.getNotices(this.state.page);
                    this.refreshNaviNumbers(this.state.page);
                  });
              }}
            >
              <i className="fas fa-chevron-right fa"></i>
            </button>
            <button
              className="home-page-text pretendard home-page-button"
              onClick={() =>
                this.setState({ page: this.state.totalPage - 1 }, () => {
                  this.getNotices(this.state.page);
                  this.refreshNaviNumbers(this.state.page);
                })
              }
            >
              끝
            </button>
          </div>
        </div>
        <HomeGuide />
      </div>
    );
  }
}
