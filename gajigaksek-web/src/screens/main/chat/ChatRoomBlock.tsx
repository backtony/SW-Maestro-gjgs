import React from "react";
import "./ChatRoomBlock.css";

interface ChatRoomBlockProps {
  chatroom: any;
  on: boolean;
  onClick: () => void;
}

export default class ChatRoomBlock extends React.Component<
  ChatRoomBlockProps,
  Record<string, never>
> {
  render() {
    return (
      <button
        className={`chat-room-block ${
          this.props.on ? "chat-room-block-on" : ""
        }`}
        onClick={() => this.props.onClick()}
      >
        <div className="chat-room-block-img">
          <img src={this.props.chatroom.userImg} />
        </div>
        <div className="chat-room-block-content">
          <div
            className={`chat-room-block-name pretendard ${
              this.props.on ? "chat-room-block-name-on" : ""
            }`}
          >
            {this.props.chatroom.users[0]}
          </div>
          <div
            className={`chat-room-block-class-name pretendard ${
              this.props.on ? "chat-room-block-class-name-on" : ""
            }`}
          >
            {this.props.chatroom.lectureTitle}
          </div>
        </div>
      </button>
    );
  }
}
