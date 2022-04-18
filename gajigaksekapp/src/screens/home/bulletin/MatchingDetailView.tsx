import React from 'react';
import {View, ScrollView, Alert} from 'react-native';
import Zone from '../../../utils/Zone';
import Category from '../../../utils/Category';
import MatchingBackModal from '../../modal/MatchingBackModal';
import UserDC from '@/services/login/UserDC';
import MatchingController from '@/services/apis/MatchingController';
import MatchingDetailViewHeader from './components/matchingDetailView/MatchingDetailViewHeader';
import MatchingDetailViewCategory from './components/matchingDetailView/MatchingDetailViewCategory';
import MatchingDetailViewWeek from './components/matchingDetailView/MatchingDetailViewWeek';
import MatchingDetailViewTime from './components/matchingDetailView/MatchingDetailViewTime';
import MatchingDetailViewPeople from './components/matchingDetailView/MatchingDetailViewPeople';
import MatchingDetailViewBottom from './components/matchingDetailView/MatchingDetailViewBottom';

interface MatchingDetailStates {
  time: string;
  clickedDayOfWeek: string[];
  people: number;
  selectedMainCategory: string;
  selectedSubCategory: string;
  modalVisible: boolean;
}

export default class MatchingDetail extends React.Component<
  {},
  MatchingDetailStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      time: '',
      clickedDayOfWeek: [],
      selectedMainCategory: '',
      selectedSubCategory: '',
      modalVisible: false,
    };
  }

  componentDidMount() {
    this.setState({
      selectedMainCategory: this.props.route.params.mainCategory,
      selectedSubCategory: '',
    });
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

  private onClickDayOfWeek(day: string) {
    let updatedList = [...this.state.clickedDayOfWeek, day];

    if (this.state.clickedDayOfWeek.includes(day)) {
      updatedList = updatedList.filter(value => value !== day);
      this.setState({clickedDayOfWeek: updatedList});
      return;
    }

    this.setState({clickedDayOfWeek: updatedList});
  }

  private checkForm(params: any) {
    if (!params.categoryId) {
      Alert.alert('소주제를 선택해주세요.');
      return true;
    }

    if (!params.dayType) {
      Alert.alert('요일을 선택해주세요.');
      return true;
    }

    if (!params.preferMemberCount) {
      Alert.alert('인원을 선택해주세요.');
      return true;
    }

    if (!params.timeType) {
      Alert.alert('시간을 선택해주세요.');
      return true;
    }

    if (!params.zoneId) {
      Alert.alert('이전 페이지에서 지역을 선택해주세요.');
      return true;
    }

    return false;
  }

  private async postMatching() {
    if (UserDC.isLogout()) {
      Alert.alert('로그인 해주세요.');
      return;
    }

    const params = {
      categoryId: Category.getIdWithSub(this.state.selectedSubCategory),
      dayType: this.state.clickedDayOfWeek.join('|'),
      preferMemberCount: this.state.people,
      timeType: this.state.time,
      zoneId: Zone.getId(this.props.route.params.subZone),
    };

    if (this.checkForm(params)) {
      return;
    }

    try {
      console.log('params: ', params);
      await MatchingController.postMatching(params);
      Alert.alert('매칭신청이 완료되었습니다.');
      this.props.navigation.navigate('bulletinMain');
    } catch (e) {
      console.error(e);
    }
  }

  render() {
    return (
      <View style={{width: '100%', flex: 1, backgroundColor: '#FFF'}}>
        <MatchingDetailViewHeader navigation={this.props.navigation} />
        <ScrollView
          style={{paddingHorizontal: 20, flex: 1}}
          showsVerticalScrollIndicator={false}
          showsHorizontalScrollIndicator={false}>
          <MatchingDetailViewCategory
            mainCategory={this.props.route.params.mainCategory}
            selectedMainCategory={this.state.selectedMainCategory}
            selectedSubCategory={this.state.selectedSubCategory}
            setSubCategory={(sub: string) =>
              this.setState({selectedSubCategory: sub})
            }
          />
          <MatchingDetailViewWeek
            clickedDayOfWeek={this.state.clickedDayOfWeek}
            onClickBigDayOfWeek={(bigDay: string) =>
              this.onClickBigDayOfWeek(bigDay)
            }
            onClickDayOfWeek={(day: string) => this.onClickDayOfWeek(day)}
            bigDayChecked={(bigDay: string) => this.bigDayChecked(bigDay)}
          />

          <MatchingDetailViewTime
            time={this.state.time}
            setTime={(time: string) => this.setState({time})}
          />

          <MatchingDetailViewPeople
            people={this.state.people}
            setPeople={(people: number) => this.setState({people})}
          />
        </ScrollView>
        <MatchingDetailViewBottom
          postMatching={() => this.postMatching()}
          setModalVisible={(visible: boolean) =>
            this.setState({modalVisible: visible})
          }
        />
        <View>
          <MatchingBackModal
            modalVisible={this.state.modalVisible}
            navigation={this.props.navigation}
            setModalVisible={(visible: boolean) => {
              this.setState({modalVisible: visible});
            }}
            setParentSubZone={(sub: string) => {
              // this.setState({profileSubZone: sub});
            }}
          />
        </View>
      </View>
    );
  }
}
