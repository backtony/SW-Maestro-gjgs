import * as React from 'react';
import {
  View,
  Text,
  StyleSheet,
  FlatList,
  ActivityIndicator,
} from 'react-native';
import firestore from '@react-native-firebase/firestore';
import auth from '@react-native-firebase/auth';
import {Bubble, GiftedChat, MessageText, Send} from 'react-native-gifted-chat';
import Icon from 'react-native-vector-icons/Ionicons';
import {STATUSBAR_HEIGHT} from '../../utils/commonParam';
import {Button} from 'react-native-elements';
import UserDC from '../login/UserDC';
import FirbaseChatRoomPaymentBlock from './FirebaseChatRoomPaymentBlock';

class FirbaseChatRoom extends React.Component<
  {},
  {
    messages: any;
    thread: any;
    nickname: string;
    avatar: string;
    memberId: string;
  }
> {
  private currentUser: any;
  private nnickname: string;
  constructor() {
    super();
    this.state = {
      thread: {
        teamId: 0,
        teamName: '그룹',
        onPressAvatar: () => {},
      },
      messages: [
        {
          _id: 0,
          text: 'New room created.',
          createdAt: new Date().getTime(),
          system: true,
        },
        // example of chat message
        {
          _id: 1,
          text: 'Henlo!',
          createdAt: new Date().getTime(),
          user: {
            _id: 2,
            name: 'Test User',
          },
        },
      ],
    };

    this.currentUser = auth().currentUser?.toJSON();
    this.state = {
      nickname: '나',
      avatar:
        'https://cdn.icon-icons.com/icons2/2506/PNG/512/user_icon_150670.png',
    };
    console.log('this.currentUser.uid: ', this.currentUser.uid);
    this.nnickname = '나';
  }

  componentDidMount() {
    const user = UserDC.getUser();
    if (user) {
      this.setState({
        nickname: user.nickname,
        avatar: user.imageFileUrl,
        memberId: user.memberId,
      });
      this.nnickname = '김기완';
    }
    this.setState({thread: this.props.route.params.thread});
    this.startMsgListener(this.props.route.params.thread.teamId);
  }

  private async sendMsg(newMsg: any = []) {
    this.setState({messages: GiftedChat.append(this.state.messages, newMsg)});

    const text = newMsg[0].text;
    firestore()
      .collection('THREADS')
      .doc(this.state.thread.teamId)
      .collection('MESSAGES')
      .add({
        text,
        createdAt: new Date().getTime(),
        user: {
          _id: this.currentUser.uid,
          email: this.currentUser.email,
          avatar: this.state.avatar,
          name: this.state.nickname,
          memberId: this.state.memberId,
        },
      });

    await firestore()
      .collection('THREADS')
      .doc(this.state.thread.teamId)
      .set(
        {
          latestMessage: {
            text,
            createdAt: new Date().getTime(),
          },
        },
        {merge: true},
      );
  }

  private startMsgListener(teamId: string) {
    const messagesListener = firestore()
      .collection('THREADS')
      .doc(teamId)
      .collection('MESSAGES')
      .orderBy('createdAt', 'desc')
      .onSnapshot(querySnapshot => {
        const messages = querySnapshot.docs.map(doc => {
          const firebaseData = doc.data();

          const data = {
            _id: doc.id,
            text: '',
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
        this.setState({messages: messages});
        console.log('msg: ', messages);
      });

    () => messagesListener();
  }

  private renderBubble(props: any) {
    console.log('props: ', props);
    console.log('user: ', props.user.name);
    // console.log('this.state.nickname: ', this.state.nickname);
    console.log('this.nnickname: ', this.nnickname);
    if (
      // props.isSameUser(props.currentMessage, props.previousMessage)
      //  && props.isSameDay(props.currentMessage, props.previousMessage)
      props.user.name === props.currentMessage.user.name
    ) {
      return (
        <Bubble
          {...props}
          wrapperStyle={{right: {backgroundColor: '#4f6cff'}}}
        />
      );
    }

    if (
      props.previousMessage &&
      props.previousMessage.user &&
      props.currentMessage.user.name === props.previousMessage.user.name
    ) {
      return (
        <Bubble
          {...props}
          wrapperStyle={{right: {backgroundColor: '#4f6cff'}}}
        />
      );
    }

    return (
      <View>
        <Text style={{marginBottom: 6}}>{props.currentMessage.user.name}</Text>
        <Bubble
          {...props}
          wrapperStyle={{right: {backgroundColor: '#4f6cff'}}}
        />
      </View>
    );
    // return (
    //   <Bubble
    //     {...props}
    //     wrapperStyle={{
    //       right: {
    //         // Here is the color change
    //         backgroundColor: '#4f6cff',
    //       },
    //     }}
    //     textStyle={{
    //       right: {
    //         color: '#fff',
    //       },
    //     }}
    //   />
    // );
  }

  private renderSend(props: any) {
    return (
      <Send {...props}>
        <View
          style={{
            width: 30,
            height: 30,
            borderRadius: 15,
            marginRight: 10,
            marginBottom: 10,
            marginTop: 10,
            backgroundColor: '#4f6cff',
            alignItems: 'center',
            justifyContent: 'center',
          }}>
          <Icon name={'arrow-up'} size={20} color={'#fff'} />
        </View>
      </Send>
    );
  }

  private renderLoading() {
    return (
      <View>
        <ActivityIndicator size="large" color="#6646ee" />
      </View>
    );
  }

  private renderMessageText(props: any) {
    const {currentMessage} = props;
    const {text: currText} = currentMessage;
    if (currText.indexOf('gajigaksekapp:payment:') === -1) {
      return <MessageText {...props} />;
    }
    return <FirbaseChatRoomPaymentBlock {...props} />;
  }

  render() {
    return (
      <View style={{flex: 1, backgroundColor: '#FFF'}}>
        <View
          style={{
            marginTop: STATUSBAR_HEIGHT,
            height: 60,
            width: '100%',
            flexDirection: 'row',
            justifyContent: 'flex-start',
            alignItems: 'center',
          }}>
          <Button
            icon={<Icon name="chevron-back" size={24} color="#1d1d1f" />}
            buttonStyle={{backgroundColor: 'white'}}
            onPress={() => this.props.navigation.goBack()}
          />
          <Text
            style={{
              // width: 93,
              // height: 20,
              fontFamily: 'NotoSansCJKkr-Bold',
              fontSize: 20,
              color: '#1d1d1f',
            }}>
            {`${
              this.state.thread ? this.state.thread.teamName : '그룹'
            }의 채팅방`}
          </Text>
        </View>

        <GiftedChat
          messages={this.state.messages}
          renderMessageText={this.renderMessageText}
          onSend={newMsg => this.sendMsg(newMsg)}
          user={{_id: this.currentUser.uid, name: this.state.nickname}}
          renderBubble={this.renderBubble}
          renderSend={this.renderSend}
          alwaysShowSend
          scrollToBottom
          renderAvatarOnTop={true}
          onPressAvatar={user => this.state.thread.onPressAvatar(user.memberId)}
          renderLoading={this.renderLoading}
          placeholder={'메시지를 입력하세요'}
        />
      </View>
    );
  }

  private styles = StyleSheet.create({
    // rest remains same
    loadingContainer: {
      flex: 1,
      alignItems: 'center',
      justifyContent: 'center',
    },
  });
}

export default FirbaseChatRoom;
