import React from "react";
import firebase from "firebase";
import Cookies from "universal-cookie";
import "./ChatView.css";
import LectureController from "../../../services/controllers/LectureController";
import { MyClassInfo } from "../myClass/MyClassView";
import ChatRoomBlock from "./ChatRoomBlock";
import ChatRoom from "./ChatRoom";

const cookies = new Cookies();

interface ChatViewStates {
  messages: any[];
  message: string;
  lectureId: number;
  lectureList: MyClassInfo[];
  chatRooms: any[];
  teamId: string;
}

export default class ChatView extends React.Component<
  Record<string, never>,
  ChatViewStates
> {
  private currentUser: any;
  private unsubscribe: any;

  constructor(props: any) {
    super(props);
    this.state = {
      messages: [],
      message: "",
      lectureId: 0,
      lectureList: [],
      chatRooms: [],
      teamId: "",
    };
    this.currentUser = cookies.get("firebase_user");
    this.getLectureList("ALL");
  }

  private async sendMsg(text: string, teamId: string) {
    const nickname = cookies.get("nickname")
      ? cookies.get("nickname")
      : "kiwankim";
    const memberId = +cookies.get("memberId");

    firebase
      .firestore()
      .collection("THREADS")
      .doc(teamId)
      .collection("MESSAGES")
      .add({
        text,
        createdAt: new Date().getTime(),
        user: {
          _id: this.currentUser.uid,
          email: this.currentUser.email,
          avatar:
            "https://images.unsplash.com/photo-1564564360647-684f24ae3e1c?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1055&q=80",
          name: nickname,
          memberId,
        },
      });

    await firebase
      .firestore()
      .collection("THREADS")
      .doc(teamId)
      .set(
        {
          latestMessage: {
            text,
            createdAt: new Date().getTime(),
          },
        },
        { merge: true }
      );
  }

  private startMsgListener(teamId: string) {
    this.unsubscribe = firebase
      .firestore()
      .collection("THREADS")
      .doc(teamId)
      .collection("MESSAGES")
      .orderBy("createdAt")
      .onSnapshot((querySnapshot: any) => {
        const messages = querySnapshot.docs.map((doc: any) => {
          const firebaseData = doc.data();

          const data = {
            _id: doc.id,
            text: "",
            createdAt: new Date().getTime(),
            ...firebaseData,
          };

          if (!firebaseData.system) {
            data.user = {
              ...firebaseData.user,
              //   name: firebaseData.user.email,
            };
          }

          return data;
        });
        this.setState({ messages: messages });
        console.log("msg: ", messages);
      });

    // () => messagesListener();
  }

  private async getLectureList(condition: string) {
    const params = {
      condition,
    };

    try {
      const res = await LectureController.getMyClass(params);
      this.setState({ lectureList: res?.data.lectureList });
      res?.data.lectureList.forEach((lecture: any) => {
        this.getChatRoomList(lecture.savedLectureId);
      });
    } catch (e) {}
  }

  private async getChatRoomList(lectureId: number) {
    if (+lectureId === 0) {
      this.state.lectureList.forEach((lecture: any) => {
        this.getChatRoomList(lecture.savedLectureId);
      });
      return;
    }
    try {
      await firebase
        .firestore()
        .collection("THREADS")
        .where("lectureId", "==", +lectureId)
        .get()
        .then((querySnapshot) => {
          querySnapshot.forEach((doc) => {
            // doc.data() is never undefined for query doc snapshots
            const chatrooms = this.state.chatRooms;
            chatrooms.push(doc.data());
            this.setState({ chatRooms: chatrooms });
          });
        })
        .catch((error) => {
          console.log("Error getting documents: ", error);
        });
    } catch (e) {}
  }

  render() {
    return (
      <div className="chat-container">
        <ChatRoom
          messages={this.state.messages}
          nickname={
            cookies.get("nickname") ? cookies.get("nickname") : "nickname"
          }
          onSend={(text: string) => {
            if (this.state.teamId) this.sendMsg(text, this.state.teamId);
          }}
        />
        <div className="chat-room-list-wrapper">
          <select
            className="chat-class-select pretendard meme2"
            value={this.state.lectureId}
            onChange={(e: any) => {
              this.setState(
                {
                  lectureId: e.target.value,
                  chatRooms: [],
                },
                () => this.getChatRoomList(this.state.lectureId)
              );
            }}
          >
            <option value={0}>전체</option>
            {this.state.lectureList.map((lec: MyClassInfo) => (
              <option value={lec.savedLectureId}>{lec.title}</option>
            ))}
          </select>
          <div className="chat-room-list">
            {this.state.chatRooms.map((chatroom: any) => (
              <ChatRoomBlock
                chatroom={chatroom}
                on={chatroom.teamId === this.state.teamId}
                onClick={() => {
                  if (this.state.teamId === chatroom.teamId) {
                    if (this.unsubscribe) this.unsubscribe();
                    this.setState({ teamId: "", messages: [] });
                    return;
                  }

                  if (this.unsubscribe) this.unsubscribe();
                  this.startMsgListener(chatroom.teamId);
                  this.setState({ teamId: chatroom.teamId });
                }}
              />
            ))}
          </div>
        </div>
        {/* <button onClick={() => this.onTest()}>test</button> */}
      </div>
    );
  }
}
