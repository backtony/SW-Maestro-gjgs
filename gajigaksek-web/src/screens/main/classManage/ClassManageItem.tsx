import React from "react";
import { strJsonType } from "../../../utils/commonParams";
import "./ClassManageItem.css";

interface ClassManageItemProps {
  idx: number;
  title: string;
  people: string;
  date: string;
  price: string;
  status1: string;
  status2: string;
}

const classTypeList: strJsonType = {
  RECRUIT: "모집 중",
  HOLD: "모집 보류",
  CANCEL: "모집 취소",
  FULL: "인원 마감",
  CLOSE: "확정 및 마감",
  END: "완료",
};

export default class ClassManageItem extends React.Component<
  ClassManageItemProps,
  Record<string, never>
> {
  render() {
    return (
      <div className="class-manage-item">
        <div className="pretendard">
          <div>{this.props.idx}</div>
        </div>
        <div className="pretendard">
          <div>{this.props.title}</div>
        </div>
        <div className="pretendard">
          <div>{this.props.people}</div>
        </div>
        <div className="pretendard">
          <div>{this.props.date}</div>
        </div>
        <div className="pretendard">
          <div>{this.props.price}</div>
        </div>
        <div className="pretendard">
          <div>{classTypeList[this.props.status1]}</div>
        </div>
        <div className="pretendard">
          <div>{this.props.status2}</div>
        </div>
      </div>
    );
  }
}
