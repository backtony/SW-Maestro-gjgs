import React from "react";
import { parseTime } from "../../../utils/commonParams";
import "./LeftComment.css";

interface LeftCommentProps {
  message: any;
}

export default class LeftComment extends React.Component<
  LeftCommentProps,
  Record<string, never>
> {
  render() {
    return (
      <div className="left-comment-wrapper">
        <div className="left-comment-img-wrapper">
          <img src={this.props.message.user.avatar} />
        </div>
        <div className="left-comment-content">
          <div className="left-comment-name pretendard">
            {this.props.message.user.name}
          </div>
          <div className="left-comment-inner-wrapper">
            <div className="left-comment-bubble pretendard">
              {this.props.message.text}
            </div>
            <div className="right-comment-time pretendard">
              {parseTime(new Date(this.props.message.createdAt))}
            </div>
          </div>
        </div>
      </div>
    );
  }
}
