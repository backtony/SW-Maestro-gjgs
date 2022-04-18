import React from "react";
import MyClassHeaderButton from "../../main/myClass/MyClassHeaderButton";
import BackofficeNoticeAddView from "./BackofficeNoticeAddView";
import BackofficeNoticeEditView from "./BackofficeNoticeEditView";

interface BackofficeNoticeViewStates {
  tab: number;
}

export default class BackofficeNoticeView extends React.Component<
  Record<string, never>,
  BackofficeNoticeViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = { tab: 0 };
  }
  render() {
    return (
      <div className="myclass-container">
        <div className="myclass-header pretendard">공지사항</div>
        <div className="myclass-list-wrapper">
          <div className="myclass-list-header">
            <MyClassHeaderButton
              title="공지사항 작성"
              on={this.state.tab === 0}
              onClick={() => this.setState({ tab: 0 })}
            />
            <MyClassHeaderButton
              title="전체 공지사항 수정"
              on={this.state.tab === 1}
              onClick={() => this.setState({ tab: 1 })}
            />
            <MyClassHeaderButton
              title="디렉터 공지사항 수정"
              on={this.state.tab === 2}
              onClick={() => this.setState({ tab: 2 })}
            />
          </div>
          <div className="myclass-splitor" />
          <div className="myclass-list">
            {this.state.tab === 0 && <BackofficeNoticeAddView />}
            {this.state.tab === 1 && (
              <BackofficeNoticeEditView noticeType={"ALL"} />
            )}
            {this.state.tab === 2 && (
              <BackofficeNoticeEditView noticeType={"DIRECTOR"} />
            )}
            <div className="myclass-splitor" />
          </div>
        </div>
      </div>
    );
  }
}
