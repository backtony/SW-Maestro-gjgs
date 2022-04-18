import * as React from 'react';
import {Text, View, ScrollView, Image, Alert, StyleSheet} from 'react-native';
import lectureApi from '../../../services/apis/lectureApi';
import FavoriteSelectModal from '../../modal/FavoriteSelectModal';
import LectureDetailDirectorProfileBlock from './components/lectureDetailView/LectureDetailViewDirectorProfileBlock';
import LectureApiController from '../../../services/apis/LectureApiController';
import UserDC from '../../../services/login/UserDC';
import firestore from '@react-native-firebase/firestore';
import GroupApiController from '@/services/apis/GroupApiController';
import ApplyApiController from '@/services/apis/ApplyApiController';
import auth from '@react-native-firebase/auth';
import User from '@/services/login/User';
import {navigate} from '@/navigation/RootNavigation';
import LectureDetailViewHeader from './components/lectureDetailView/LectureDetailViewHeader';
import LectureDetailViewInfoBlock from './components/lectureDetailView/LectureDetailViewInfoBlock';
import LectureDetailViewPeopleBlock from './components/lectureDetailView/LectureDetailViewPeopleBlock';
import LectureDetailViewDateSelect from './components/lectureDetailView/LectureDetailViewDateSelect';
import LectureDetailViewTimeSelect from './components/lectureDetailView/LectureDetailViewTimeSelect';
import LectureDetailViewPaymentTypeBlock from './components/lectureDetailView/LectureDetailViewPaymentTypeBlock';
import LectureDetailViewTeamSelect from './components/lectureDetailView/LectureDetailVIewTeamSelect';
import LectureDetailViewTabView from './components/lectureDetailView/LectureDetailViewTabView';
import LectureDetailViewRecommendList from './components/lectureDetailView/LectureDetailViewRecommendList';
import LectureDetailViewBottom from './components/lectureDetailView/LectureDetailViewBottom';
import FloatingAddButton from '@/components/FloatingAddButton';
import LectureDetailCouponButton from './components/lectureDetailView/LectureDetailCouponButton';

export class ScheduleInfo {
  scheduleId: number | undefined;
  lectureDate: string | undefined;
  currentParticipants: number | undefined;
  minParticipants: number | undefined;
  maxParticipants: number | undefined;
  startHour: number | undefined;
  startMinute: number | undefined;
  endHour: number | undefined;
  endMinute: number | undefined;
  progressMinutes: number | undefined;
  scheduleStatus: string | undefined;
}

interface LectureDetailViewStates {
  data: any;
  memberEdit: boolean;
  favorites: number[];
  favoriteModalVisible: boolean;

  selectedDate: string;
  selectedTime: string;

  questionList: any[];

  lecture: any;

  selectedSchedule: ScheduleInfo;

  applyType: string;
  applyTeamDropDown: boolean;

  teamList: any;
  selectedGroup: number;

  width: number;
}

export default class LectureDetailView extends React.Component<
  {},
  LectureDetailViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      data: null,
      memberEdit: false,
      favorites: [],
      favoriteModalVisible: false,

      selectedDate: '',
      selectedTime: '',

      questionList: [],

      lecture: {
        categoryId: 1,
        curriculum: 'curriculumTest0',
        finalPrice: 50000,
        lectureId: 1,
        lectureTitle: 'testTitle0',
        mainText: 'mainText0',
        maxPeople: 5,
        minPeople: 2,
        priceFour: 20000,
        priceOne: 50000,
        priceThree: 30000,
        priceTwo: 40000,
        progressTime: 3,
        thumbnailImageUrl: 'testUrl0',
        zoneId: 1,
        questionResponseList: [],
        directorResponse: {
          directorId: 1,
          directorProfileImageUrl: 'test',
          directorProfileText: 'test',
        },
      },

      applyType: 'PERSONAL',

      applyTeamDropDown: false,
      teamList: [],
      selectedGroup: 0,

      width: 0,
    };
    this.getPossibleTeamList();
  }
  componentDidMount() {
    console.log('lectureId: ', this.props.route.params.lectureId);
    this.setState({
      data: lectureApi(null).lectureList,
      favorites: lectureApi(null)
        .lectureList.filter(value => !!value.favorite)
        .map(value => value.lectureId),
    });
    this.getLectureDash(this.props.route.params.lectureId);
    this.getQuestion(this.props.route.params.lectureId);
    this.props.navigation.addListener('didFocus', payload => {
      this.forceUpdate();
    });
  }

  private async getLectureDash(lectureId: number) {
    try {
      const res = await LectureApiController.getLectureDash({lectureId});
      let lecture = res.data;
      lecture.imageUrl = lecture.thumbnailImageUrl;
      lecture.myFavorite = lecture.myFavoriteLecture;
      lecture.title = lecture.lectureTitle;
      this.setState({lecture: res.data});
    } catch (e) {
      console.error(e);
    }
  }

  private async getQuestion(lectureId: number) {
    try {
      const res = await LectureApiController.getQuestion({lectureId});
      this.setState({questionList: res.data.content});
    } catch (e) {
      console.error(e);
    }
  }

  private toggleFavorite = (id: number) => {
    const updatedFavorites = this.state.favorites;
    if (updatedFavorites.includes(id)) {
      updatedFavorites.splice(updatedFavorites.indexOf(id), 1);
    } else {
      updatedFavorites.push(id);
    }

    this.setState({favorites: updatedFavorites});
  };

  private async getPossibleTeamList() {
    if (UserDC.isLogout()) {
      return;
    }

    try {
      const res = await GroupApiController.getPossibleTeamList({});
      this.setState({teamList: res?.data.myLeadTeams}, () =>
        console.log('teamList: ', this.state.teamList),
      );
    } catch (e) {
      console.error(e);
    }
  }

  private checkForm(params: any) {
    if (!params.scheduleId) {
      Alert.alert('스케쥴을 선택하세요.');
      return false;
    }
    if (params.teamId === 0) {
      Alert.alert('팀을 선택해주세요.');
      return false;
    }
    if (!params.teamId) {
      Alert.alert('팀id 를 가져올수없습니다.');
      return false;
    }

    return true;
  }

  private async postSchedule(
    scheduleId: number | undefined,
    lectureId: number,
    teamId: number,
  ) {
    const params = {scheduleId, lectureId, teamId};

    if (!this.checkForm(params)) {
      return;
    }

    try {
      await ApplyApiController.postApply(params);
      Alert.alert('신청이 완료되었습니다.');
      this.sendLinkMessage(params);
    } catch (e) {}
  }

  private async sendLinkMessage(params: any) {
    const url = `gajigaksekapp:payment:scheduleId=${params.scheduleId}&lectureId=${params.lectureId}&teamId=${params.teamId}`;
    const currentUser: any = auth().currentUser?.toJSON();
    const user: User = UserDC.getUser();
    const teamName: string = this.state.teamList.filter(
      (team: any) => team.teamId === params.teamId,
    )[0].teamName;

    const pp = {
      _id: currentUser.uid,
      email: currentUser.email,
      avatar: user.imageFileUrl,
      name: user.nickname,
      memberId: user.memberId,
      params,
    };

    const text = url;
    firestore()
      .collection('THREADS')
      .doc(JSON.stringify(params.teamId))
      .collection('MESSAGES')
      .add({
        text,
        createdAt: new Date().getTime(),
        user: {
          _id: currentUser.uid,
          email: currentUser.email,
          avatar: user.imageFileUrl,
          name: user.nickname,
          memberId: user.memberId,
        },
      });

    navigate('chatroom', {
      thread: {
        teamId: JSON.stringify(params.teamId),
        teamName: teamName,
        onPressAvatar: (memberId: number) => {
          navigate('profile', {memberId});
        },
      },
    });
  }

  render() {
    return (
      <View
        style={{flex: 1, backgroundColor: '#FFF'}}
        onLayout={event => {
          var {width} = event.nativeEvent.layout;
          this.setState({width});
        }}>
        <LectureDetailViewHeader
          navigation={this.props.navigation}
          lecture={this.state.lecture}
        />
        <ScrollView
          showsVerticalScrollIndicator={false}
          showsHorizontalScrollIndicator={false}>
          <Image
            style={this.styles.image}
            source={{
              uri: this.state.lecture.thumbnailImageUrl,
            }}
          />
          <Text style={this.styles.text}>
            {this.state.lecture.lectureTitle}
          </Text>
          <LectureDetailViewInfoBlock lecture={this.state.lecture} />
          <LectureDetailViewPeopleBlock lecture={this.state.lecture} />
          <View style={this.styles.wrapper}>
            <LectureDetailDirectorProfileBlock
              profile={{
                nickname: '이수빈',
                info: this.state.lecture.directorResponse.directorProfileText,
                image:
                  this.state.lecture.directorResponse.directorProfileImageUrl,
                directorId: this.state.lecture.directorResponse.directorId,
              }}
              navigation={this.props.navigation}
            />
          </View>
          <LectureDetailViewDateSelect
            lecture={this.state.lecture}
            selectedDate={this.state.selectedDate}
            setSelectedDate={(date: string) =>
              this.setState({selectedDate: date})
            }
          />
          <LectureDetailViewTimeSelect
            lecture={this.state.lecture}
            selectedDate={this.state.selectedDate}
            selectedSchedule={this.state.selectedSchedule}
            setSchedule={(schedule: ScheduleInfo) => {
              this.setState({
                selectedSchedule: schedule,
                selectedTime: `${schedule.startHour}:${
                  schedule.startMinute === 0 ? '00' : '30'
                } ~ ${schedule.endHour}:${
                  schedule.endMinute === 0 ? '00' : '30'
                }`,
              });
            }}
          />

          <LectureDetailViewPaymentTypeBlock
            applyType={this.state.applyType}
            setApplyType={(applyType: string) => this.setState({applyType})}
          />
          {this.state.applyType === 'TEAM' && (
            <LectureDetailViewTeamSelect
              applyTeamDropDown={this.state.applyTeamDropDown}
              selectedGroup={this.state.selectedGroup}
              teamList={this.state.teamList}
              setApplyTeamDropDown={(applyTeamDropDown: boolean) =>
                this.setState({applyTeamDropDown})
              }
              setTeam={(callback: any) => {
                this.setState(
                  state => ({
                    selectedGroup: callback(state.selectedGroup),
                  }),
                  () => console.log(this.state.selectedGroup),
                );
              }}
            />
          )}
          <LectureDetailViewTabView
            navigation={this.props.navigation}
            width={this.state.width}
            questionList={this.state.questionList}
            lecture={this.state.lecture}
            refreshParent={() =>
              this.getLectureDash(this.state.lecture.lectureId)
            }
          />

          <LectureDetailViewRecommendList
            navigation={this.props.navigation}
            data={this.state.data}
            modalVisible={this.state.favoriteModalVisible}
            toggleFavorite={(lectureId: number) =>
              this.toggleFavorite(lectureId)
            }
            setModalVisible={(modalVisible: boolean) =>
              this.setState({favoriteModalVisible: modalVisible})
            }
          />
        </ScrollView>
        <View>
          <LectureDetailCouponButton lectureId={this.state.lecture.lectureId} />
        </View>

        <LectureDetailViewBottom
          lecture={this.state.lecture}
          navigation={this.props.navigation}
          favoriteModalVisible={this.state.favoriteModalVisible}
          toggleFavorite={(lectureId: number) => this.toggleFavorite(lectureId)}
          setFavoriteModalVisible={(modalVisible: boolean) =>
            this.setState({favoriteModalVisible: modalVisible})
          }
          onPressPayment={async () => {
            if (
              this.state.selectedSchedule &&
              this.state.applyType === 'TEAM'
            ) {
              this.postSchedule(
                this.state.selectedSchedule.scheduleId,
                this.state.lecture.lectureId,
                this.state.selectedGroup,
              );
            } else if (
              this.state.selectedSchedule &&
              this.state.applyType === 'PERSONAL'
            ) {
              navigate('payment', {
                lectureId: this.state.lecture.lectureId,
                scheduleId: this.state.selectedSchedule.scheduleId,
                teamId: 0,
                payType: 'PERSONAL',
                selectedDate: this.state.selectedSchedule.lectureDate,
                selectedTime: this.state.selectedTime,
                price: this.state.lecture.priceOne,
              });
            } else {
              Alert.alert('스케줄을 선택해주세요.');
            }
          }}
        />
        <View>
          <FavoriteSelectModal
            modalVisible={this.state.favoriteModalVisible}
            lecture={this.state.lecture}
            setModalVisible={(visible: boolean) => {
              this.setState({favoriteModalVisible: visible});
            }}
            setFavorite={() => {
              this.setState({data: []}, () => {
                this.getLectureDash(this.state.lecture.lectureId);
              });
            }}
          />
        </View>
      </View>
    );
  }
  private styles = StyleSheet.create({
    image: {
      height: 350,
    },
    text: {
      fontSize: 20,
      lineHeight: 24,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 20,
      marginBottom: 10,
      color: '#070707',
      marginHorizontal: 20,
    },
    wrapper: {
      height: 100,
      marginTop: 40,
      backgroundColor: '#4f6cff',
    },
  });
}
