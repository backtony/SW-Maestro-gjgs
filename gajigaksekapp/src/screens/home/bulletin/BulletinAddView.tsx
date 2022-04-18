import * as React from 'react';
import {Text, View, StyleSheet, Alert} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import GroupApiController from '../../../services/apis/GroupApiController';
import DropDownPicker from 'react-native-dropdown-picker';
import UserDC from '../../../services/login/UserDC';
import BulletinApiController from '../../../services/apis/BulletinApiController';
import {KeyboardAwareScrollView} from 'react-native-keyboard-aware-scroll-view';
import LectureApiController from '../../../services/apis/LectureApiController';
import BulletinAddViewHeader from './components/bulletinAddView/BulletinAddViewHeader';
import BulletinAddViewTitleInput from './components/bulletinAddView/BulletinAddViewTitleInput';
import BulletinAddViewAgeList from './components/bulletinAddView/BulletinAddViewAgeList';
import BulletinAddViewDayList from './components/bulletinAddView/BulletinAddViewDayList';
import BulletinAddViewTimeList from './components/bulletinAddView/BulletinAddViewTimeList';
import BulletinAddViewTextInput from './components/bulletinAddView/BulletinAddViewTextInput';
import BulletinAddViewLectureSelect from './components/bulletinAddView/BulletinAddViewLectureSelect';
import BulletinAddViewButton from './components/bulletinAddView/BulletinAddViewButton';

export default class BulletinAddView extends React.Component<
  {},
  {
    title: string;
    clickedDayOfWeek: string[];
    profileSubZone: string[];
    mainCategory: string;
    subCategory: number[];
    time: string[];
    people: string;
    dropDown: boolean;
    selectedGroup: number;
    text: string;

    teamList: any[];

    selectedLectureId: number;
    age: string;
    selectedLecture: any;
  }
> {
  constructor() {
    super();
    this.state = {
      title: '',
      clickedDayOfWeek: [],
      mainCategory: '액티비티',
      subCategory: [1, 2],
      time: [],
      dropDown: false,
      selectedGroup: 0,
      profileSubZone: [],
      text: '',
      teamList: [],
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
    this.getPossibleTeamList();
    this.getLecture(1);
  }

  private toggleDropDown() {
    this.setState({dropDown: !this.state.dropDown});
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

    this.setState({time: ['MORNING', 'NOON', 'AFTERNOON']});
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

  private checkForm(params: any) {
    const {age, dayType, lectureId, teamId, text, timeType, title} = params;

    if (!age) {
      alert('연령을 선택해주세요.');
      return false;
    }

    if (!dayType) {
      alert('요일을 선택해주세요.');
      return false;
    }

    if (!lectureId) {
      alert('클래스를 선택해주세요.');
      return false;
    }

    if (!text) {
      alert('본문을 작성해주세요.');
      return false;
    }

    if (text.length < 10 || text.length > 100) {
      alert('본문은 10자이상 100자이하로 적어주세요.');
      return false;
    }

    if (!timeType) {
      alert('가능한 시간을 선택해주세요.');
      return false;
    }

    if (!title) {
      alert('제목을 입력해주세요.');
      return false;
    }

    return true;
  }

  private async getPossibleTeamList() {
    if (UserDC.isLogout()) {
      alert('로그인을 시도해주세요.');
      return;
    }

    try {
      const res = await GroupApiController.getPossibleTeamList({});
      this.setState({teamList: res?.data.myLeadTeams});
    } catch (e) {
      console.error(e);
    }
  }

  private async postCreateBulletinApi() {
    const params = {
      age: this.state.age,
      dayType: this.state.clickedDayOfWeek.join('|'),
      lectureId: this.state.selectedLectureId,
      teamId: this.state.selectedGroup,
      text: this.state.text,
      timeType: this.state.time.join('|'),
      title: this.state.title,
    };

    if (!this.checkForm(params)) {
      return;
    }

    if (this.state.selectedGroup === 0) {
      try {
        const res = await GroupApiController.createTeam({
          categoryList: [2],
          dayType: params.dayType,
          maxPeople: 4,
          teamName: params.title,
          zoneId: 2,
          timeType: params.timeType,
        });
        params.teamId = res?.data.createdTeamId;
      } catch (e) {
        console.error(e);
        return;
      }
    }

    try {
      await BulletinApiController.postCreateBulletin(params);
      this.props.navigation.goBack();
      alert('게시글 생성이 완료되었습니다.');
    } catch (e) {
      alert('게시글 생성을 다시 시도해주세요.');
      console.error(e);
    }
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

  private async checkTeam(teamId: number) {
    if (teamId === 0) {
      return;
    }
    const team = this.state.teamList.filter(item => item.teamId === teamId)[0];

    if (!team.hasBulletin) {
      return;
    }

    if (team.teamsRecruit) {
      Alert.alert(
        '알림',
        '현재 활성화된 게시글이 존재합니다. 해당 게시글로 이동할까요?',
        [
          {
            text: '아니오',
            onPress: () => this.props.navigation.goBack(),
            style: 'cancel',
          },
          {
            text: '예',
            onPress: () =>
              this.props.navigation.navigate('bulletinDash', {
                bulletinId: team.bulletinData.bulletinId,
                myFavorite: false,
                refreshParent: () => {
                  // this.getBulletinListApi();
                },
              }),
          },
        ],
      );
      return;
    } else if (!team.teamsRecruit) {
      team.lectureData.thumbnailImageUrl = team.lectureData.lectureImageUrl;
      Alert.alert('알림', '이전에 작성한 게시글 정보가 있습니다. 불러올까요?', [
        {
          text: '아니오',
          onPress: () => console.log('Cancel Pressed'),
          style: 'cancel',
        },
        {
          text: '예',
          onPress: () => {
            this.setState({
              age: team.bulletinData.age,
              title: team.bulletinData.bulletinTitle,
              text: team.bulletinData.text,
              selectedLectureId: team.lectureData.lectureId,
              selectedLecture: team.lectureData,
              time: team.bulletinData.time.split('|'),
              clickedDayOfWeek: team.bulletinData.day.split('|'),
            });
          },
        },
      ]);
      return;
    }
  }

  render() {
    return (
      <View style={{flex: 1, backgroundColor: '#FFF'}}>
        <BulletinAddViewHeader navigation={this.props.navigation} />
        <KeyboardAwareScrollView
          style={{paddingHorizontal: 20}}
          nestedScrollEnabled={true}
          contentContainerStyle={{flexGrow: 1, zIndex: 500}}>
          <Text style={this.styles.title}>그룹</Text>
          <DropDownPicker
            open={this.state.dropDown}
            value={this.state.selectedGroup}
            items={[
              {
                label: '새로운 그룹 생성',
                value: 0,
                icon: () => <Icon name="add" size={12} color="#4f6cff" />,
              },
              ...this.state.teamList.map(item => ({
                label: item.teamName,
                value: item.teamId,
              })),
            ]}
            setOpen={() => {
              this.toggleDropDown();
            }}
            setValue={callback => {
              this.setState(
                state => ({
                  selectedGroup: callback(state.selectedGroup),
                }),
                () => {
                  this.checkTeam(this.state.selectedGroup);
                },
              );
            }}
            setItems={() => {}}
            showTickIcon={false}
            style={this.styles.dropDown}
            listItemContainerStyle={this.styles.listItemContainer}
            listItemLabelStyle={{color: '#4a4a4c', fontSize: 12}}
            dropDownContainerStyle={this.styles.dropDownContainer}
            arrowIconStyle={this.styles.arrowIcon}
            textStyle={{fontSize: 14}}
          />
          <BulletinAddViewTitleInput
            title={this.state.title}
            setTitle={(title: string) => this.setState({title})}
          />
          <BulletinAddViewAgeList
            age={this.state.age}
            setAge={(age: string) => this.setState({age})}
          />
          <BulletinAddViewDayList
            clickedDayOfWeek={this.state.clickedDayOfWeek}
            onClickDayOfWeek={(day: string) => this.onClickDayOfWeek(day)}
            onClickBigDayOfWeek={(bigDay: string) =>
              this.onClickBigDayOfWeek(bigDay)
            }
            bigDayChecked={(bigDay: string) => this.bigDayChecked(bigDay)}
          />
          <BulletinAddViewTimeList
            onClickTime={(time: string) => this.onClickTime(time)}
            onClickTimeBig={() => this.onClickTimeBig()}
            time={this.state.time}
          />

          <BulletinAddViewTextInput
            text={this.state.text}
            setText={(text: string) => this.setState({text})}
          />
          <BulletinAddViewLectureSelect
            lectureInfo={this.state.selectedLecture}
            getLecture={(lectureId: number) => this.getLecture(lectureId)}
          />
        </KeyboardAwareScrollView>
        <BulletinAddViewButton onPress={() => this.postCreateBulletinApi()} />
      </View>
    );
  }

  private styles = StyleSheet.create({
    dropDown: {
      backgroundColor: '#fafafb',
      borderWidth: 0,
      height: 34,
      borderRadius: 6,
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
    },
    listItemContainer: {
      height: 30,
      backgroundColor: '#fafafb',
    },
    dropDownContainer: {
      borderWidth: 0,
    },
    arrowIcon: {
      width: 18,
      height: 18,
    },
    title: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 20,
      marginBottom: 10,
    },
  });
}
