import { navigate } from '@/navigation/RootNavigation';
import React from 'react';
import {Alert, Image, Text, View} from 'react-native';
import {TouchableOpacity} from 'react-native-gesture-handler';
import {MessageText} from 'react-native-gifted-chat';
import LectureApiController from '../apis/LectureApiController';

interface FirbaseChatRoomPaymentBlockStates {
  thumbnailImageUrl: string;
  lectureTitle: string;
  scheduleId: number;
  lectureId: number;
  teamId: number;
}

export default class FirbaseChatRoomPaymentBlock extends React.Component<
  any,
  FirbaseChatRoomPaymentBlockStates
> {
  private text: string;
  private currentMessage: any;
  constructor(props: any) {
    super(props);

    const {currentMessage} = props;
    this.currentMessage = currentMessage;
    const {text} = currentMessage;
    this.text = text;

    this.state = {
      thumbnailImageUrl: '',
      lectureTitle: '',
      scheduleId: 0,
      lectureId: 0,
      teamId: 0,
    };

  }

  componentDidMount(){
    this.parseUrl(this.text);
  }

  private navigateToPayment() {
    navigate('payment', {lectureId: this.state.lectureId, teamId: this.state.teamId, scheduleId: this.state.scheduleId, payType: "TEAM"});
  }

  private parseUrl(url: string) {
    const params = url.split('gajigaksekapp:payment:')[1];
    const scheduleId: number = +params.split('scheduleId=')[1].split('&')[0];
    const lectureId: number = +params.split('&lectureId=')[1].split('&')[0];
    const teamId: number = +params.split('&teamId=')[1];

    
    this.setState({scheduleId, lectureId, teamId});
    this.getLecture(lectureId);
  }

  private async getLecture(lectureId: number) {
    try {
      const res = await LectureApiController.getLectureDash({
        lectureId: lectureId,
      });

      this.setState({
        thumbnailImageUrl: res.data.thumbnailImageUrl,
        lectureTitle: res.data.lectureTitle,
      });
    } catch (e) {
      console.error(e);
    }
  }

  render() {
    return (
      <TouchableOpacity
        style={{margin: 10}}
        onPress={() => this.navigateToPayment()}>
        <View style={{flexDirection: 'row'}}>
          <Image
            style={{width: 50, height: 50}}
            source={{
              uri: this.state.thumbnailImageUrl,
            }}
          />
          <View style={{margin: 5}}>
            <Text numberOfLines={1} style={{fontFamily: 'NotoSansCJKkr-Regular', fontSize: 16, color: 'white', width: 200}}>
              {this.state.lectureTitle}
            </Text>
            <Text style={{fontFamily: 'NotoSansCJKkr-Regular', fontSize: 16, color: 'white'}}>결제하러 가기 > </Text>
          </View>
        </View>
        {/* <MessageText
          {...this.props}
          textStyle={{
            left: {
              color: '#F11',
            },
            right: {
              color: '#F11',
            },
          }}
          currentMessage={{
            ...this.currentMessage,
            text: this.text,
          }}
        /> */}
      </TouchableOpacity>
    );
  }
}
