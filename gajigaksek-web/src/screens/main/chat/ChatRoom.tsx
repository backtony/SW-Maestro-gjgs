import React from "react";
import "./ChatRoom.css";
import LeftComment from "./LeftComment";
import RightComment from "./RightComment";

interface ChatRoomProps {
  messages: any[];
  nickname: string;
  onSend: (text: string) => void;
}

interface ChatRoomStates {
  input_msg: string;
}

export default class ChatRoom extends React.Component<
  ChatRoomProps,
  ChatRoomStates
> {
  constructor(props: any) {
    super(props);
    this.state = { input_msg: "" };
  }
  render() {
    return (
      <div className="chat-wrapper">
        <div className="chat-scrollView">
          {this.props.messages.map((message: any) => {
            if (message.user.name === this.props.nickname) {
              return <RightComment message={message} />;
            }
            return <LeftComment message={message} />;
          })}
        </div>
        <div className="myclass-splitor" />
        <div className="chat-bottom">
          <div className="chat-input-wrapper">
            <input
              className="pretendard"
              placeholder="메시지를 입력하세요"
              value={this.state.input_msg}
              onChange={(e: any) =>
                this.setState({ input_msg: e.target.value })
              }
            ></input>
          </div>

          <button
            className="chat-send-btn"
            onClick={() => {
              this.props.onSend(this.state.input_msg);
              this.setState({ input_msg: "" });
            }}
          >
            <i className="fas fa-arrow-up fa-lg"></i>
          </button>
        </div>
      </div>
    );
  }
}
