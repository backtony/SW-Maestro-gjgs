import * as React from 'react';
import {View, Alert} from 'react-native';
import ZoneSelectModal from '../modal/ZoneSelectModal';
import GroupApiController from '../../services/apis/GroupApiController';
import Zone from '../../utils/Zone';
import {getTimeInKorean} from '../../utils/commonFunctions';
import GroupEditViewHeader from './components/groupEditView/GroupEditViewHeader';
import GroupEditScrollView from './components/groupEditView/groupEditScrollView/GroupEditScrollView';
import UserDC from '@/services/login/UserDC';
import GroupEditViewBottom from './components/groupEditView/GroupEditViewBottom';

const timeTypeList = [
  ['오전', 'MORNING'],
  ['오후', 'NOON'],
  ['저녁', 'AFTERNOON'],
];

const peopleType = ['2인', '3인', '4인'];

export default class GroupEditView extends React.Component<
  {},
  {
    title: string;
    clickedDayOfWeek: string[];
    modalVisible: boolean;
    profileSubZone: string;
    subCategory: number[];
    time: string[];
    people: string;
    clickedMember: string;
    memberList: any[];
  }
> {
  constructor() {
    super();
    this.state = {
      clickedDayOfWeek: [],
      modalVisible: false,
      subCategory: [2, 3],
      time: [],
      memberList: [],
    };
  }

  componentDidMount() {
    this.initData();
  }

  private async initData() {
    const groupData = this.props.route.params.team;
    const dayOfWeekList = groupData.day.split('|');
    const timeList = groupData.time
      .split('|')
      .map(time => getTimeInKorean(time));
    const memberId = (await UserDC.getUser()).memberId;

    this.setState({
      title: groupData.teamName,
      clickedDayOfWeek: dayOfWeekList,
      time: timeList,
      people: `${groupData.maxPeople}인`,
      profileSubZone: Zone.getZone(groupData.zoneId)[1],
      memberList: groupData.teamMemberList.filter(
        member => member.memberId !== memberId,
      ),
      subCategory: groupData.categoryList,
    });
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
      JSON.stringify(['오전', '오후', '저녁'])
    ) {
      this.setState({time: []});
      return;
    }

    this.setState({time: ['오전', '오후', '저녁']});
  }

  private makeTimeParam() {
    const {time} = this.state;

    return time
      .map(hour => {
        let code: string = '';
        timeTypeList.some(value => {
          if (value[0] === hour) {
            code = value[1];
            return true;
          }
        });

        return code;
      })
      .join('|');
  }

  private makePeopleParam() {
    let peopleParam: number = 1;
    peopleType.some((value, index) => {
      if (value === this.state.people) {
        peopleParam = index + 2;
        return true;
      }
    });
    return peopleParam;
  }

  private async editGroupApi() {
    if (!this.checkForm()) {
      return;
    }

    const {title, profileSubZone, subCategory} = this.state;

    const dayType = this.state.clickedDayOfWeek.join('|');
    const timeType = this.makeTimeParam();
    const peopleType = this.makePeopleParam();
    const zoneType = Zone.getId(profileSubZone);

    try {
      const res = await GroupApiController.editTeam(
        {
          teamName: title,
          maxPeople: peopleType,
          zoneId: zoneType,
          categoryList: subCategory,
          dayType: dayType,
          timeType: timeType,
        },
        this.props.route.params.team.teamId,
      );

      console.log('GroupEdit: ', res);
      alert('그룹편집이 완료되었습니다. ');
      this.props.route.params.refreshParent();
      this.props.navigation.goBack();
    } catch (e) {
      console.error(e);
    }
  }
  private checkForm() {
    const {title, clickedDayOfWeek, profileSubZone, subCategory, time, people} =
      this.state;

    if (!title) {
      alert('그룹명을 입력해주세요.');
      return false;
    }

    if (clickedDayOfWeek.length <= 0) {
      alert('요일을 선택해주세요.');
      return false;
    }

    if (time.length <= 0) {
      console.log('time: ', time);
      alert('시간대를 선택해주세요.');
      return false;
    }

    if (!people) {
      alert('인원수를 선택해주세요.');
      return false;
    }

    if (!profileSubZone) {
      alert('위치를 선택해주세요.');
      return false;
    }

    if (subCategory.length <= 0) {
      alert('공통태그를 선택해주세요.');
      return false;
    }

    return true;
  }

  private onClickDelegate() {
    const memberInfo = this.state.memberList.filter(
      member => member.nickname === this.state.clickedMember,
    );

    if (memberInfo.length <= 0) {
      Alert.alert('멤버를 선택해주세요.');
      return;
    }

    Alert.alert('알림', '해당 멤버로 리더를 변경하시겠습니까?', [
      {
        text: '아니요',
        onPress: () => console.log('Cancel Pressed'),
        style: 'cancel',
      },
      {
        text: '예',
        onPress: () => this.patchLeader(this.patchLeader(memberInfo)),
      },
    ]);
  }

  private async patchLeader(memberInfo: any) {
    const params = {
      memberId: memberInfo[0].memberId,
      teamId: this.props.route.params.team.teamId,
    };

    try {
      await GroupApiController.patchLeader(params);
      this.props.route.params.refreshParent();
      this.props.navigation.goBack();
    } catch (e) {}
  }

  render() {
    return (
      <View style={{flex: 1, backgroundColor: '#FFF'}}>
        <GroupEditViewHeader navigation={this.props.navigation} />
        <GroupEditScrollView
          navigation={this.props.navigation}
          title={this.state.title}
          setTitle={(title: string) => this.setState({title})}
          memberList={this.state.memberList}
          clickedMember={this.state.clickedMember}
          onClickMember={(nickname: string) => {
            if (this.state.clickedMember === nickname) {
              this.setState({clickedMember: ''});
              return;
            }
            this.setState({clickedMember: nickname});
          }}
          onClickBigDayOfWeek={(bigDay: string) =>
            this.onClickBigDayOfWeek(bigDay)
          }
          bigDayChecked={(bigDay: string) => this.bigDayChecked(bigDay)}
          clickedDayOfWeek={this.state.clickedDayOfWeek}
          onClickDayOfWeek={(day: string) => this.onClickDayOfWeek(day)}
          onClickTime={(time: string) => this.onClickTime(time)}
          time={this.state.time}
          onClickTimeBig={() => this.onClickTimeBig()}
          people={this.state.people}
          setPeople={(people: string) => this.setState({people})}
          subZone={this.state.profileSubZone}
          setSubZone={(subZone: string) =>
            this.setState({profileSubZone: subZone})
          }
          subCategory={this.state.subCategory}
          setSubCategory={(idList: number[]) =>
            this.setState({subCategory: idList})
          }
          onClickDelegate={() => this.onClickDelegate()}
        />
        <GroupEditViewBottom onPress={() => this.editGroupApi()} />
        <View>
          <ZoneSelectModal
            modalVisible={this.state.modalVisible}
            setModalVisible={(visible: boolean) => {
              this.setState({modalVisible: visible});
            }}
            setParentSubZone={(sub: string) => {
              this.setState({profileSubZone: sub});
            }}
          />
        </View>
      </View>
    );
  }
}
