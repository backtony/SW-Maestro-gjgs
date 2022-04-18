import React from "react";
import { parseTime } from "../../../utils/commonParams";
import "./RightComment.css";

interface RightCommentProps {
  message: any;
}

export default class RightComment extends React.Component<
  RightCommentProps,
  Record<string, never>
> {
  render() {
    return (
      <div className="right-comment-wrapper">
        <div className="right-comment-time pretendard">
          {parseTime(new Date(this.props.message.createdAt))}
        </div>
        <div className="right-comment-bubble pretendard">
          {this.props.message.text}
        </div>
      </div>
    );
  }
}
