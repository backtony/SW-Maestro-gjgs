import * as React from 'react';
import {View, Alert} from 'react-native';
import ZoneSelectModal from '../modal/ZoneSelectModal';
import Category from '../../utils/Category';
import GroupApiController from '../../services/apis/GroupApiController';
import Zone from '../../utils/Zone';
import FirebaseAddChatRoom from '../../services/firebaseAuth/FirebaseAddChatRoom';
import GroupAddViewHeader from './components/groupAddView/GroupAddViewHeader';
import GroupAddScrollView from './components/groupAddView/GroupAddScrollView.tsx/GroupAddScrollView';
import CategorySelectModal from '../modal/CategorySelectModal';
import GroupAddViewBottom from './components/groupAddView/GroupAddViewBottom';
import {StyleSheet} from 'react-native';

interface GroupAddViewStates {
  title: string;
  clickedDayOfWeek: string[];
  modalVisible: boolean;
  subZone: string;
  subCategory: number[];
  time: string[];
  people: number;
  categoryModalVisible: boolean;
}

export default class GroupAddView extends React.Component<
  {},
  GroupAddViewStates
> {
  constructor() {
    super();
    this.state = {
      clickedDayOfWeek: [],
      modalVisible: false,
      categoryModalVisible: false,
      subCategory: [],
      time: [],
    };
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
      JSON.stringify(['AFTERNOON', 'MORNING', 'NOON'])
    ) {
      this.setState({time: []});
      return;
    }

    this.setState({time: ['AFTERNOON', 'MORNING', 'NOON']});
  }

  private async createGroupApi() {
    if (!this.checkForm()) {
      return;
    }

    const {title, subZone: profileSubZone, subCategory} = this.state;

    const dayType = this.state.clickedDayOfWeek.join('|');
    const timeType = this.state.time.join('|');
    const peopleType = this.state.people;
    const zoneType = Zone.getId(profileSubZone);

    console.log('timeType: ', timeType);

    try {
      const res = await GroupApiController.createTeam({
        teamName: title,
        maxPeople: peopleType,
        zoneId: zoneType,
        categoryList: subCategory,
        dayType: dayType,
        timeType: timeType,
      });
      console.log('GroupAdd: ', res);
      FirebaseAddChatRoom.addNewChatRoom(title, res?.data.createdTeamId);
      this.props.route.params.parentRefresh();
      this.props.navigation.goBack();
      Alert.alert('그룹이 생성되었습니다. ');
    } catch (e) {
      console.error(e);
    }
  }

  private checkForm() {
    const {
      title,
      clickedDayOfWeek,
      subZone: profileSubZone,
      subCategory,
      time,
      people,
    } = this.state;

    if (!title) {
      Alert.alert('그룹명을 입력해주세요.');
      return false;
    }

    if (clickedDayOfWeek.length <= 0) {
      Alert.alert('요일을 선택해주세요.');
      return false;
    }

    if (time.length <= 0) {
      console.log('time: ', time);
      Alert.alert('시간대를 선택해주세요.');
      return false;
    }

    if (!people) {
      Alert.alert('인원수를 선택해주세요.');
      return false;
    }

    if (!profileSubZone) {
      Alert.alert('위치를 선택해주세요.');
      return false;
    }

    if (subCategory.length <= 0) {
      Alert.alert('공통태그를 선택해주세요.');
      return false;
    }

    return true;
  }

  render() {
    return (
      <View style={this.styles.container}>
        <GroupAddViewHeader navigation={this.props.navigation} />
        <GroupAddScrollView
          navigation={this.props.navigation}
          title={this.state.title}
          clickedDayOfWeek={this.state.clickedDayOfWeek}
          time={this.state.time}
          people={this.state.people}
          modalVisible={this.state.modalVisible}
          categoryModalVisible={this.state.categoryModalVisible}
          subZone={this.state.subZone}
          subCategory={this.state.subCategory}
          setTitle={(title: string) => this.setState({title})}
          onClickBigDayOfWeek={(bigDay: string) =>
            this.onClickBigDayOfWeek(bigDay)
          }
          onClickDayOfWeek={(day: string) => this.onClickDayOfWeek(day)}
          bigDayChecked={(bigDay: string) => this.bigDayChecked(bigDay)}
          onClickTime={(time: string) => this.onClickTime(time)}
          onClickTimeBig={() => this.onClickTimeBig()}
          setPeople={(people: number) => this.setState({people})}
          setModalVisible={(modalVisible: boolean) =>
            this.setState({modalVisible})
          }
          setCategoryModalVisible={(categoryModalVisible: boolean) =>
            this.setState({categoryModalVisible})
          }
          setSubCategory={(idList: number[]) =>
            this.setState({subCategory: idList})
          }
        />
        <GroupAddViewBottom onPress={() => this.createGroupApi()} />
        <View>
          <ZoneSelectModal
            modalVisible={this.state.modalVisible}
            setModalVisible={(visible: boolean) => {
              this.setState({modalVisible: visible});
            }}
            setParentSubZone={(sub: string) => {
              this.setState({subZone: sub});
            }}
          />
        </View>
        <View>
          <CategorySelectModal
            modalVisible={this.state.categoryModalVisible}
            setModalVisible={(visible: boolean) => {
              this.setState({categoryModalVisible: visible});
            }}
            selectedCategoryList={this.state.subCategory.map(
              id => Category.getCategory(id)[1],
            )}
            setSubCategory={(sub: string[]) => {
              this.setState({
                subCategory: sub.map(ss => Category.getIdWithSub(ss)),
              });
            }}
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {flex: 1, backgroundColor: '#FFF'},
  });
}
