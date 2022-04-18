import * as React from 'react';
import {View} from 'react-native';
import BulletinApiController from '../../../services/apis/BulletinApiController';
import {CommonActions} from '@react-navigation/native';
import {KeyboardAwareScrollView} from 'react-native-keyboard-aware-scroll-view';
import BulletinEditViewHeader from './components/bulletinEditView/BulletinEditViewHeader';
import BulletinEditViewTitle from './components/bulletinEditView/BulletinEditViewTitle';
import BulletinEditViewAge from './components/bulletinEditView/BulletinEditViewAge';
import BulletinEditViewWeek from './components/bulletinEditView/BulletinEditViewWeek';
import BulletinEditViewTime from './components/bulletinEditView/BulletinEditViewTime';
import BulletinEditViewMainText from './components/bulletinEditView/BulletinEditViewMainText';
import BulletinAddViewLectureSelect from './components/bulletinAddView/BulletinAddViewLectureSelect';
import LectureApiController from '@/services/apis/LectureApiController';
import BulletinEditViewBottom from './components/bulletinEditView/BulletinEditViewBottom';

interface BulletinEditViewStates {
  bulletinTitle: string;
  clickedDayOfWeek: string[];
  time: string[];
  age: string;
  bulletinText: string;

  lectureId: number;
  selectedLecture: any;
  selectedLectureId: number;
}
export default class BulletinEditView extends React.Component<
  {},
  BulletinEditViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      bulletinTitle: '',
      clickedDayOfWeek: [],
      time: [],
      age: 'TWENTY_TO_TWENTYFIVE',
      bulletinText: '',
      lectureId: 1,
      selectedLectureId: 1,
      selectedLecture: {
        lectureId: 1,
        lectureTitle: '16년차 베이커리 장인의 케이크 클래스',
        priceFour: 20000,
        priceOne: 50000,
        priceThree: 30000,
        priceTwo: 40000,
        thumbnailImageUrl: 'https://via.placeholder.com/400x200/FFB6C1/000000',
        zoneId: 1,
      },
    };
    this.getLecture(1);
  }

  componentDidMount() {
    const team = this.props.route.params.team;

    this.setState(
      {
        bulletinText: team.bulletinText,
        bulletinTitle: team.bulletinTitle,
        clickedDayOfWeek: team.day.split('|'),
        time: team.time.split('|'),
        age: team.age,
        lectureId: team.bulletinsLecture.lectureId,
      },
      () => {
        console.log(this.state);
      },
    );
  }
  private onClickDayOfWeek(day: string) {
    let updatedList = [...this.state.clickedDayOfWeek, day];

    if (this.state.clickedDayOfWeek.includes(day)) {
      updatedList = updatedList.filter(value => value !== day);
      this.setState({clickedDayOfWeek: updatedList});
      return;
    }

    this.setState({clickedDayOfWeek: updatedList});
  }

  private bigDayChecked(bigDay: string): boolean {
    return (
      (bigDay === '주중' &&
        JSON.stringify(this.state.clickedDayOfWeek) ===
          JSON.stringify(['MON', 'TUE', 'WED', 'THU', 'FRI'])) ||
      (bigDay === '주말' &&
        JSON.stringify(this.state.clickedDayOfWeek) ===
          JSON.stringify(['SUN', 'SAT'])) ||
      (bigDay === '무관' &&
        JSON.stringify(this.state.clickedDayOfWeek) ===
          JSON.stringify(['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT']))
    );
  }

  private onClickTime(time: string) {
    let updatedList = [...this.state.time, time];

    if (this.state.time.includes(time)) {
      updatedList = updatedList.filter(value => value !== time);
      this.setState({time: updatedList});
      return;
    }

    this.setState({time: updatedList});
  }

  private onClickTimeBig() {
    if (
      JSON.stringify(this.state.time.sort()) ===
      JSON.stringify(['AFTERNOON', 'MORNING', 'NOON'])
    ) {
      this.setState({time: []});
      return;
    }

    this.setState({time: ['AFTERNOON', 'MORNING', 'NOON']});
  }

  private onClickBigDayOfWeek(bigDay: string) {
    const reset: boolean = this.bigDayChecked(bigDay);

    if (reset) {
      this.setState({clickedDayOfWeek: []});
      return;
    }

    if (bigDay === '주중') {
      this.setState({clickedDayOfWeek: ['MON', 'TUE', 'WED', 'THU', 'FRI']});
    }

    if (bigDay === '주말') {
      this.setState({clickedDayOfWeek: ['SUN', 'SAT']});
    }

    if (bigDay === '무관') {
      this.setState({
        clickedDayOfWeek: ['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT'],
      });
    }
  }

  private async postBulletinEdit() {
    const params = {
      age: this.state.age,
      dayType: this.state.clickedDayOfWeek.join('|'),
      lectureId: this.state.selectedLectureId,
      text: this.state.bulletinText,
      timeType: this.state.time.join('|'),
      title: this.state.bulletinTitle,
      teamId: this.props.route.params.team.bulletinsTeam.teamId,
    };

    if (!this.checkForm(params)) {
      return;
    }
    console.log('params: ', params);

    try {
      await BulletinApiController.patchBulletinEdit(
        params,
        this.props.route.params.team.bulletinId,
      );
      alert('게시글이 수정되었습니다.');
      this.props.navigation.navigate('bulletinMain');
    } catch (e) {
      console.error(e);
    }

    console.log('params: ', params);
  }

  private async deleteBulletin() {
    try {
      const res = await BulletinApiController.deleteBulletin({
        bulletinId: this.props.route.params.team.bulletinId,
      });
      alert('게시글이 삭제되었습니다.');
      this.props.navigation.dispatch(CommonActions.navigate('bulletinMain'));
    } catch (e) {
      console.error(e);
    }
  }

  private checkForm(params: any) {
    if (!params.title) {
      alert('제목을 입력해주세요.');
      return false;
    }

    if (!params.age) {
      alert('연령을 입력해주세요.');
      return false;
    }

    if (!params.dayType) {
      alert('요일을 선택해주세요.');
      return false;
    }

    if (!params.lectureId) {
      alert('클래스를 선택해주세요.');
      return false;
    }

    if (!params.text) {
      alert('본문을 입력해주세요.');
      return false;
    }

    if (params.text.length < 10 || params.text.length > 100) {
      alert('본문은 10자이상 100자이하만 가능합니다.');
      return false;
    }

    if (!params.timeType) {
      alert('시간대를 선택해주세요.');
      return false;
    }

    return true;
  }

  private async getLecture(lectureId: number) {
    try {
      const res = await LectureApiController.getLectureDash({
        lectureId: lectureId,
      });
      this.setState({
        selectedLecture: res.data,
        selectedLectureId: res.data.lectureId,
      });
    } catch (e) {
      console.error(e);
    }
  }

  render() {
    return (
      <View style={{flex: 1, backgroundColor: '#FFF'}}>
        <BulletinEditViewHeader navigation={this.props.navigation} />
        <KeyboardAwareScrollView style={{paddingHorizontal: 20}}>
          <BulletinEditViewTitle
            title={this.state.bulletinTitle}
            setTitle={(text: string) => this.setState({bulletinTitle: text})}
          />
          <BulletinEditViewAge
            age={this.state.age}
            setAge={(age: string) => this.setState({age})}
          />
          <BulletinEditViewWeek
            clickedDayOfWeek={this.state.clickedDayOfWeek}
            onClickBigDayOfWeek={(bigDay: string) =>
              this.onClickBigDayOfWeek(bigDay)
            }
            onClickDayOfWeek={(day: string) => this.onClickDayOfWeek(day)}
            bigDayChecked={(bigDay: string) => this.bigDayChecked(bigDay)}
          />
          <BulletinEditViewTime
            onClickTime={(time: string) => this.onClickTime(time)}
            onClickTimeBig={() => this.onClickTimeBig()}
            time={this.state.time}
          />

          <BulletinEditViewMainText
            text={this.state.bulletinText}
            setText={(text: string) => this.setState({bulletinText: text})}
          />
          <BulletinAddViewLectureSelect
            lectureInfo={this.state.selectedLecture}
            getLecture={(lectureId: number) => this.getLecture(lectureId)}
          />
        </KeyboardAwareScrollView>
        <BulletinEditViewBottom
          onPressEdit={() => this.postBulletinEdit()}
          onPressDelete={() => this.deleteBulletin()}
        />
      </View>
    );
  }
}
