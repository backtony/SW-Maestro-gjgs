import React from 'react';
import {
  StyleSheet,
  Image,
  Text,
  View,
  ScrollView,
  TextInput,
  Modal,
  Pressable,
  TouchableOpacity,
} from 'react-native';
import {STATUSBAR_HEIGHT} from '../../../utils/commonParam';
import Icon from 'react-native-vector-icons/Ionicons';
import {Button} from 'react-native-elements';
import ZoneSelectModal from '../../modal/ZoneSelectModal';
import UserDC from '../../../services/login/UserDC';
import Zone from '../../../utils/Zone';
import {KeyboardAwareScrollView} from 'react-native-keyboard-aware-scroll-view';

export default class FirstLogin extends React.Component<
  {},
  {
    name: string;
    phone: string;
    nickname: string;
    gender: string;
    age: number;
    profileSubZone: string;
    phoneNumber: string;
    modalVisible: boolean;
    user: User;
    recommendNickname: string;
  }
> {
  constructor() {
    super();
    this.state = {
      name: '',
      phone: '',
      nickname: '',
      gender: '',
      age: 0,
      profileSubZone: '',
      phoneNumber: '',
      modalVisible: false,
      user: UserDC.getUser(),
      recommendNickname: '',
    };
  }
  componentDidMount() {
    const user = UserDC.getUser();
    if (user && user.nickname) {
      this.setState({nickname: user.nickname, name: user.nickname});
    }

    if (user) {
      this.setState({user: user});
    }
  }

  private onPhoneNumberChanged(text: string) {
    let newText = '';
    let numbers = '0123456789';

    for (var i = 0; i < text.length; i++) {
      if (numbers.indexOf(text[i]) > -1) {
        newText = newText + text[i];
      } else {
        // your call back function
        alert('숫자만 입력해주세요.');
      }
    }
    this.setState({phoneNumber: newText});
  }

  private onAgeChanged(text: string) {
    let newText = '';
    let numbers = '0123456789';

    for (var i = 0; i < text.length; i++) {
      if (numbers.indexOf(text[i]) > -1) {
        newText = newText + text[i];
      } else {
        // your call back function
        alert('숫자만 입력해주세요.');
      }
    }
    this.setState({age: +newText});
  }

  private checkKorean(text: string) {
    const regex = /^[ㄱ-ㅎ|가-힣]+$/;

    if (regex.test(text)) {
      this.setState({name: text});
      return;
    }

    alert('이름은 한글만 입력 가능합니다.');
  }

  private checkForm() {
    const {name, nickname, age, phoneNumber, gender, profileSubZone} =
      this.state;

    const koreanRegex = /^[가-힣]{2,6}$/;
    if (!koreanRegex.test(name)) {
      alert('이름을 다시 입력해주세요. (한글, 2자이상, 6자이하)');
      return false;
    }

    const nicknameRegex = /^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{2,20}$/;
    if (!nickname || !nicknameRegex.test(nickname)) {
      alert('닉네임을 다시 입력해주세요. (특수문자X, 2자이상 20자이하)');
      return false;
    }

    if (!phoneNumber || (phoneNumber.length < 10 && phoneNumber.length > 11)) {
      alert('번호를 다시 입력해주세요. (10자이상 11자이하)');
      return false;
    }

    if (!age || age < 10 || age > 100) {
      alert('나이를 다시 입력해주세요. (10세-100세)');
      return false;
    }

    if (!gender) {
      alert('성별을 선택해주세요');
      return false;
    }

    if (!profileSubZone) {
      alert('위치를 선택해주세요.');
      return false;
    }

    return true;
  }
  render() {
    return (
      <View style={{backgroundColor: '#FFF', flex: 1}}>
        <View
          style={{
            marginTop: STATUSBAR_HEIGHT,
            height: 60,
            width: '100%',
            flexDirection: 'row',
            justifyContent: 'space-between',
            alignItems: 'center',
          }}>
          <View style={{flexDirection: 'row', alignItems: 'center'}}>
            <Button
              icon={<Icon name="chevron-back" size={24} color="#1d1d1f" />}
              buttonStyle={{backgroundColor: 'white'}}
              onPress={() => {
                alert(
                  '정보를 입력해주시지 않으면 회원가입이 완료되지 않습니다.',
                );
                this.props.navigation.goBack();
              }}
            />
            <Text
              style={{
                // width: 93,
                // height: 20,
                fontFamily: 'NotoSansCJKkr-Bold',
                fontSize: 20,
                color: '#1d1d1f',
              }}>
              내정보 입력하기
            </Text>
          </View>
          <Button
            buttonStyle={{
              backgroundColor: '#FFF',
              marginRight: 10,
              marginTop: 10,
            }}
            titleStyle={{
              color: '#4f6cff',
              fontSize: 14,
              lineHeight: 16,
              fontFamily: 'NotoSansCJKkr-Regular',
            }}
            onPress={() => {
              if (!this.checkForm()) {
                return;
              }

              this.props.navigation.navigate('firstSubCategory', {
                id: this.state.user.id,
                imageFileUrl: this.state.user.imageFileUrl,
                name: this.state.name,
                phone: this.state.phoneNumber,
                nickname: this.state.nickname,
                age: this.state.age,
                sex: this.state.gender,
                zoneId: Zone.getId(this.state.profileSubZone),
                recommendNickname: this.state.recommendNickname,
              });
            }}
            title={'다음'}
          />
        </View>
        <KeyboardAwareScrollView enableOnAndroid={true}>
          <Text
            style={{
              fontSize: 16,
              lineHeight: 18,
              fontFamily: 'NotoSansCJKkr-Bold',
              marginLeft: 20,
              marginTop: 20,
              marginBottom: 10,
            }}>
            이름
          </Text>
          <View
            style={{
              marginHorizontal: 20,
              backgroundColor: 'rgba(245, 245, 247, 0.6)',
              height: 44,
              borderRadius: 6,
              flexDirection: 'row',
              justifyContent: 'space-between',
              alignItems: 'center',
            }}>
            <TextInput
              style={{
                height: 44,
                width: '70%',
                maxLength: 15,
                paddingLeft: 13,
              }}
              onChangeText={text => {
                this.checkKorean(text);
              }}
              value={this.state.name}
            />
            {/* <Text style={{marginLeft: 13}}>{this.state.name}</Text> */}
          </View>
          <Text
            style={{
              fontSize: 16,
              lineHeight: 18,
              fontFamily: 'NotoSansCJKkr-Bold',
              marginLeft: 20,
              marginTop: 20,
              marginBottom: 10,
            }}>
            휴대폰 번호
          </Text>
          <View
            style={{
              marginHorizontal: 20,
              backgroundColor: 'rgba(245, 245, 247, 0.6)',
              height: 44,
              borderRadius: 6,
              flexDirection: 'row',
              justifyContent: 'space-between',
              alignItems: 'center',
            }}>
            <TextInput
              style={{
                height: 44,
                width: '70%',
                maxLength: 15,
                paddingLeft: 13,
              }}
              keyboardType="numeric"
              onChangeText={text => {
                this.onPhoneNumberChanged(text);
              }}
              value={this.state.phoneNumber}
            />
          </View>
          <Text
            style={{
              fontSize: 16,
              lineHeight: 18,
              fontFamily: 'NotoSansCJKkr-Bold',
              marginLeft: 20,
              marginTop: 20,
              marginBottom: 10,
            }}>
            닉네임
          </Text>
          <View
            style={{
              marginHorizontal: 20,
              backgroundColor: 'rgba(245, 245, 247, 0.6)',
              height: 44,
              borderRadius: 6,
              flexDirection: 'row',
              justifyContent: 'space-between',
              alignItems: 'center',
            }}>
            <TextInput
              style={{
                height: 44,
                width: '70%',
                maxLength: 15,
                paddingLeft: 13,
              }}
              onChangeText={text => {
                this.setState({nickname: text});
              }}
              value={this.state.nickname}
            />
          </View>
          <Text
            style={{
              fontSize: 16,
              lineHeight: 18,
              fontFamily: 'NotoSansCJKkr-Bold',
              marginLeft: 20,
              marginTop: 20,
              marginBottom: 10,
            }}>
            성별
          </Text>
          <View
            style={{
              flexDirection: 'row',
              marginHorizontal: 20,
            }}>
            <TouchableOpacity
              style={[
                {
                  flexDirection: 'row',
                  justifyContent: 'flex-start',
                  flex: 1,
                  marginRight: 10,
                  height: 44,
                  alignItems: 'center',
                  borderRadius: 6,
                  backgroundColor: 'rgba(245, 245, 247, 0.6)',
                },
                this.state.gender === 'M' ? {backgroundColor: '#4f6cff'} : {},
              ]}
              onPress={() => {
                this.setState({gender: 'M'});
              }}>
              <Icon
                name="male-sharp"
                size={14}
                color={this.state.gender === 'M' ? '#FFF' : '#4f6cff'}
                style={{marginLeft: 12, marginRight: 10}}
              />
              <Text
                style={[
                  this.styles.genderText,
                  this.state.gender === 'M' ? {color: '#FFF'} : {},
                ]}>
                {'남자'}
              </Text>
            </TouchableOpacity>

            <TouchableOpacity
              style={[
                {
                  flexDirection: 'row',
                  justifyContent: 'flex-start',
                  flex: 1,
                  height: 44,
                  alignItems: 'center',
                  borderRadius: 6,
                  backgroundColor: 'rgba(245, 245, 247, 0.6)',
                },
                this.state.gender === 'F' ? {backgroundColor: '#ff4f4f'} : {},
              ]}
              onPress={() => {
                this.setState({gender: 'F'});
              }}>
              <Icon
                name="female-sharp"
                size={14}
                color={this.state.gender === 'F' ? '#FFF' : '#ff4f4f'}
                style={{marginLeft: 12, marginRight: 10}}
              />
              <Text
                style={[
                  this.styles.genderText,
                  this.state.gender === 'F' ? {color: '#FFF'} : {},
                ]}>
                {'여성'}
              </Text>
            </TouchableOpacity>
          </View>
          <Text
            style={{
              fontSize: 16,
              lineHeight: 18,
              fontFamily: 'NotoSansCJKkr-Bold',
              marginLeft: 20,
              marginTop: 20,
              marginBottom: 10,
            }}>
            나이
          </Text>
          <View
            style={{
              marginHorizontal: 20,
              backgroundColor: 'rgba(245, 245, 247, 0.6)',
              height: 44,
              borderRadius: 6,
              flexDirection: 'row',
              justifyContent: 'space-between',
              alignItems: 'center',
            }}>
            <TextInput
              style={{
                height: 44,
                width: '70%',
                paddingLeft: 13,
              }}
              keyboardType="numeric"
              onChangeText={text => {
                this.onAgeChanged(text);
              }}
              value={this.state.age}
            />
          </View>
          <View
            style={{
              flexDirection: 'row',
              justifyContent: 'space-between',
              alignItems: 'center',
            }}>
            <Text
              style={{
                fontSize: 16,
                lineHeight: 18,
                fontFamily: 'NotoSansCJKkr-Bold',
                marginLeft: 20,
                marginTop: 20,
                marginBottom: 10,
              }}>
              위치
            </Text>
            <Button
              buttonStyle={{
                width: 43,
                height: 28,
                borderRadius: 6,
                backgroundColor: 'tarnparent',
                marginRight: 13,
                paddingTop: 8,
                paddingBottom: 5,
              }}
              titleStyle={{
                color: '#4f6cff',
                fontSize: 12,
                lineHeight: 14,
                fontFamily: 'NotoSansCJKkr-Regular',
              }}
              onPress={() =>
                this.setState({modalVisible: !this.state.modalVisible})
              }
              title={'편집'}
            />
          </View>
          <View
            style={{
              marginHorizontal: 20,
              backgroundColor: 'rgba(245, 245, 247, 0.6)',
              height: 44,
              borderRadius: 6,
              flexDirection: 'row',
              justifyContent: 'space-between',
              alignItems: 'center',
            }}>
            <Text
              style={{
                // height: 44,
                width: '70%',
                paddingLeft: 13,
                // alignSelf: 'center',
              }}>
              {this.state.profileSubZone}
            </Text>
          </View>
          <Text
            style={{
              fontSize: 16,
              lineHeight: 18,
              fontFamily: 'NotoSansCJKkr-Bold',
              marginLeft: 20,
              marginTop: 20,
              marginBottom: 10,
            }}>
            추천인
          </Text>
          <View
            style={{
              marginHorizontal: 20,
              backgroundColor: 'rgba(245, 245, 247, 0.6)',
              height: 44,
              borderRadius: 6,
              flexDirection: 'row',
              justifyContent: 'space-between',
              alignItems: 'center',
            }}>
            <TextInput
              style={{
                height: 44,
                width: '70%',
                paddingLeft: 13,
              }}
              onChangeText={text => {
                this.setState({recommendNickname: text});
              }}
              value={this.state.recommendNickname}
            />
          </View>
          <View style={{height: 100}} />
        </KeyboardAwareScrollView>
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
          {/* <Text style={this.styles.header}>{this.state.profileSubZone}</Text> */}
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    genderText: {
      fontSize: 14,
    },
  });
}
