import React from "react";
import NoticeController from "../../../services/controllers/NoticeController";
import { NoticeInfo } from "../../main/home/bulletin/HomeBulletin";
import BackofficeNoticeBlock from "./BackofficeNoticeBlock";

const noticeDummy = [
  {
    title: "11번가와 함께하는 프로모션",
    text: "",
    createdDate: "202110312222",
    noticeId: 1,
  },
  {
    title: "G마켓과 함께하는 프로모션",
    text: "",
    createdDate: "202110312222",
    noticeId: 2,
  },
  {
    title: "솜씨당과 함께하는 프로모션",
    text: "",
    createdDate: "202110312222",
    noticeId: 3,
  },
  {
    title: "쿠팡과 함께하는 프로모션",
    text: "",
    createdDate: "202110312222",
    noticeId: 4,
  },
];

interface BackofficeNoticeEditViewProps {
  noticeType: string;
}

interface BackofficeNoticeEditViewStates {
  notices: NoticeInfo[];
  page: number;
  openedIds: number[];
}

export default class BackofficeNoticeEditView extends React.Component<
  BackofficeNoticeEditViewProps,
  BackofficeNoticeEditViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = { notices: [], page: 0, openedIds: [] };
    this.getNotices(props.noticeType);
  }

  private async getNotices(noticeType: string) {
    try {
      let res;
      if (noticeType === "DIRECTOR")
        res = await NoticeController.getNotices(this.state.page);
      else if (noticeType === "ALL")
        res = await NoticeController.getAllNotices(this.state.page);
      if (!res) return;
      this.setState({ notices: res?.data.content });
    } catch (e) {}
  }
  render() {
    return (
      <div className="question-manage-list">
        {this.state.notices.map((item: NoticeInfo, index: number) => (
          <div>
            <BackofficeNoticeBlock
              key={index}
              notice={item}
              noticeType={this.props.noticeType}
              open={this.state.openedIds.includes(index)}
              onClick={() => {
                let ids = this.state.openedIds;

                if (ids.includes(index)) {
                  ids = ids.filter((id: number) => id !== index);
                } else {
                  ids.push(index);
                }
                this.setState({ openedIds: ids });
              }}
            />
            <div className="myclass-splitor" />
          </div>
        ))}
      </div>
    );
  }
}
