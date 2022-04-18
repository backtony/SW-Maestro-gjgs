import React from 'react';
import {
  StyleSheet,
  Image,
  Text,
  View,
  ScrollView,
  TextInput,
  TouchableOpacity,
} from 'react-native';
import {launchImageLibrary} from 'react-native-image-picker';
import Category from '../../utils/Category';
import Zone from '../../utils/Zone';
import {Button} from 'react-native-elements';
import ZoneSelectModal from '../modal/ZoneSelectModal';
import {STATUSBAR_HEIGHT} from '../../utils/commonParam';
import Icon from 'react-native-vector-icons/Ionicons';
import MypageApiController from '../../services/apis/MypageApiController';

export class FileForm {
  public uri: string;
  public type: string;
  public name: string;
}

class editMyProfile extends React.Component<
  {},
  {
    imageBase64: string;
    uploadUri: string;
    nickname: string;
    categoryIdList: number[];
    isMale: boolean;
    age: number;
    modalVisible: boolean;
    selectedMainZone: string;
    selectedSubZone: string;
    gender: string;
    profileSubZone: string;
    //
    imageFileUrl: string;
    name: string;
    phone: string;
    profileText: string;
  }
> {
  constructor(props: any) {
    super(props);
    this.state = {
      imageBase64: '',
      uploadUri: '',
      nickname: '홍길동',
      categoryIdList: [],
      isMale: true,
      age: 10,
      modalVisible: false,
      selectedMainZone: '서울',
      selectedSubZone: '강남/역삼/선릉/삼성',
      profileSubZone: '강남/역삼/선릉/삼성',
      gender: 'M',
      //
      imageFileUrl: 'https://bootdey.com/img/Content/avatar/avatar6.png',
      name: '홍길동',
      phone: '',
      profileText: '',
    };
  }

  componentDidMount() {
    const {
      imageFileUrl,
      nickname,
      name,
      phone,
      memberCategoryId,
      profileText,
      gender,
      age,
      zoneId,
    } = this.props.route.params;
    this.setState({
      imageFileUrl: imageFileUrl,
      nickname: nickname,
      name: name,
      phone: phone,
      categoryIdList: memberCategoryId,
      profileText: profileText,
      gender: gender,
      age: age,
      selectedSubZone: Zone.getZone(zoneId)[1],
      profileSubZone: Zone.getZone(zoneId)[1],
    });
  }

  private onChangeCategory(idList: number[]) {
    this.setState({categoryIdList: idList});
  }

  private async putNickNameEdit(nickname: string) {
    const nicknameRegex = /^[ㄱ-ㅎ가-힣a-z0-9_-]{2,20}$/;
    if (!nickname || !nicknameRegex.test(nickname)) {
      alert('닉네임을 다시 입력해주세요. (특수문자X, 2자이상 20자이하)');
      return false;
    }

    try {
      await MypageApiController.putNicknameEdit({
        nickname: nickname,
      });
      alert(`닉네임이 변경되었습니다. (${nickname})`);
    } catch (e) {
      console.error(e);
      return;
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
    this.setState({phone: newText});
  }

  private async putPhoneEdit(phoneNumber: string) {
    if (!phoneNumber || (phoneNumber.length < 10 && phoneNumber.length > 11)) {
      alert('번호를 다시 입력해주세요. (10자이상 11자이하)');
      return false;
    }

    try {
      await MypageApiController.putPhoneEdit({
        phone: phoneNumber,
      });
      alert(`휴대폰번호가 변경되었습니다. (${phoneNumber})`);
    } catch (e) {
      console.error(e);
      return;
    }
  }

  private async putTextEdit(text: string) {
    try {
      await MypageApiController.putTextEdit({
        profileText: text,
      });
      alert('소개가 변경되었습니다.');
    } catch (e) {
      console.error(e);
      return;
    }
  }

  private async putZone(text: string) {
    try {
      await MypageApiController.putZone({
        zoneId: Zone.getId(text),
      });

      this.setState({profileSubZone: text});
      alert('지역이 변경되었습니다.');
    } catch (e) {
      console.error(e);
      return;
    }
  }

  private async postImage(file: FileForm) {
    try {
      await MypageApiController.postImage(file);
    } catch (e) {
      console.error(e);
      return;
    }
  }

  render() {
    return (
      <View style={{backgroundColor: '#FFF'}}>
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
              onPress={() => this.props.navigation.goBack()}
            />
            <Text style={this.styles.text}>프로필 편집</Text>
          </View>
          <Button
            buttonStyle={{backgroundColor: '#FFF', marginRight: 10}}
            titleStyle={this.styles.buttonTitle}
            title={'완료'}
          />
        </View>
        <ScrollView>
          <View style={{width: 80, marginLeft: 20, marginTop: 20}}>
            <Image
              style={this.styles.avatar}
              source={{
                uri: this.state.uploadUri
                  ? this.state.uploadUri
                  : this.state.imageFileUrl,
              }}
            />
            <TouchableOpacity
              style={this.styles.button}
              onPress={() => {
                launchImageLibrary(
                  {mediaType: 'photo', quality: 0.5},
                  ({assets}) => {
                    if (assets && assets[0].uri) {
                      console.log('assets: ', assets);
                      this.postImage({
                        uri: assets[0].uri,
                        type: assets[0].type ? assets[0].type : 'image/jpg',
                        name: assets[0].fileName
                          ? assets[0].fileName
                          : 'image.jpg',
                      });
                      // // this.setState({imageBase64: assets[0].base64});
                      this.setState({uploadUri: assets[0].uri});
                    }
                  },
                );
              }}>
              <Icon name="camera" size={18} color="#FFF" />
            </TouchableOpacity>
          </View>
          <Text style={this.styles.text2}>닉네임</Text>
          <View style={this.styles.wrapper}>
            <TextInput
              style={{
                height: 44,
                width: '70%',
                maxLength: 15,
                paddingLeft: 13,
              }}
              onChangeText={text => this.setState({nickname: text})}
              value={this.state.nickname}
            />
            <Button
              buttonStyle={this.styles.button2}
              titleStyle={this.styles.buttonTitle2}
              onPress={() => {
                this.putNickNameEdit(this.state.nickname);
              }}
              title={'변경'}
            />
          </View>
          <Text style={this.styles.text3}>이름</Text>
          <View style={this.styles.wrapper2}>
            <Text style={{marginLeft: 13}}>
              {this.props.route && this.props.route.params
                ? this.props.route.params.name
                : this.state.nickname}
            </Text>
          </View>
          <Text style={this.styles.text4}>휴대폰 번호</Text>
          <View style={this.styles.wrapper3}>
            <TextInput
              style={{
                height: 44,
                width: '70%',
                maxLength: 15,
                paddingLeft: 13,
              }}
              onChangeText={text => this.onPhoneNumberChanged(text)}
              value={this.state.phone}
            />
            <Button
              buttonStyle={this.styles.button3}
              titleStyle={this.styles.buttonTitle3}
              onPress={() => this.putPhoneEdit(this.state.phone)}
              title={'변경'}
            />
          </View>
          <View>
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
                소개
              </Text>
              <Button
                buttonStyle={{
                  width: 43,
                  hegith: 28,
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
                onPress={() => {
                  this.putTextEdit(this.state.profileText);
                }}
                title={'변경'}
              />
            </View>
          </View>
          <View
            style={{
              marginHorizontal: 20,
              backgroundColor: 'rgba(245, 245, 247, 0.6)',
              height: 184,
              borderRadius: 6,
              flexDirection: 'row',
              justifyContent: 'space-between',
              alignItems: 'flex-start',
            }}>
            <TextInput
              style={{
                maxLength: 150,
                marginHorizontal: 13,
                marginTop: 5,
                numberOfLines: 5,
              }}
              multiline
              value={this.state.profileText}
              onChangeText={text => this.setState({profileText: text})}
              placeholder={'소개를 입력해주세요.'}
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
            관심분야
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
                  width: '80%',
                  // paddingLeft: 13,
                  // alignSelf: 'center',
                }}
                numberOfLines={1}>
                {this.state.categoryIdList
                  .map(id => `#${Category.getCategory(id)[1]}`)
                  .join(' ')}
              </Text>
            </View>
            <Button
              buttonStyle={{
                width: 43,
                hegith: 28,
                borderRadius: 6,
                backgroundColor: '#4f6cff',
                marginRight: 13,
                paddingTop: 8,
                paddingBottom: 5,
              }}
              titleStyle={{
                color: '#FFF',
                fontSize: 12,
                lineHeight: 14,
                fontFamily: 'NotoSansCJKkr-Regular',
              }}
              onPress={() => {
                this.props.navigation.navigate('editCategory', {
                  setSubCategory: this.onChangeCategory.bind(this),
                  selectedSubIdList: this.state.categoryIdList,
                });
              }}
              title={'변경'}
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
            <View
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
              ]}>
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
            </View>

            <View
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
              ]}>
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
            </View>
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
                maxLength: 15,
                paddingLeft: 13,
              }}
              value={JSON.stringify(this.state.age)}
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
                hegith: 28,
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
          <View style={{height: 150}} />
        </ScrollView>

        <View>
          <ZoneSelectModal
            modalVisible={this.state.modalVisible}
            setModalVisible={(visible: boolean) => {
              this.setState({modalVisible: visible});
            }}
            setParentSubZone={(sub: string) => {
              this.putZone(sub);
            }}
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    buttonTitle3: {
      color: '#FFF',
      fontSize: 12,
      lineHeight: 14,
      fontFamily: 'NotoSansCJKkr-Regular',
    },
    button3: {
      width: 43,
      height: 28,
      borderRadius: 6,
      backgroundColor: '#4f6cff',
      marginRight: 13,
      paddingTop: 8,
      paddingBottom: 5,
    },
    wrapper3: {
      marginHorizontal: 20,
      backgroundColor: 'rgba(245, 245, 247, 0.6)',
      height: 44,
      borderRadius: 6,
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
    },
    text4: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginLeft: 20,
      marginTop: 20,
      marginBottom: 10,
    },
    wrapper2: {
      marginHorizontal: 20,
      backgroundColor: 'rgba(245, 245, 247, 0.6)',
      height: 44,
      borderRadius: 6,
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
    },
    text3: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginLeft: 20,
      marginTop: 20,
      marginBottom: 10,
    },
    buttonTitle2: {
      color: '#FFF',
      fontSize: 12,
      lineHeight: 14,
      fontFamily: 'NotoSansCJKkr-Regular',
    },
    button2: {
      width: 43,
      height: 28,
      borderRadius: 6,
      backgroundColor: '#4f6cff',
      marginRight: 13,
      paddingTop: 8,
      paddingBottom: 5,
    },
    wrapper: {
      marginHorizontal: 20,
      backgroundColor: 'rgba(245, 245, 247, 0.6)',
      height: 44,
      borderRadius: 6,
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
    },
    text2: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginLeft: 20,
      marginTop: 20,
      marginBottom: 10,
    },
    button: {
      position: 'absolute',
      right: 0,
      bottom: 0,
      backgroundColor: '#4f6cff',
      borderRadius: 24,
      width: 28,
      height: 28,
      alignItems: 'center',
      justifyContent: 'center',
    },
    buttonTitle: {
      color: '#4f6cff',
      fontSize: 14,
      lineHeight: 16,
      fontFamily: 'NotoSansCJKkr-Regular',
    },
    text: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 20,
      color: '#1d1d1f',
    },
    avatar: {
      width: 80,
      height: 80,
      borderRadius: 80,
      borderWidth: 1,
      borderColor: '#f5f5f7',
    },
    header: {
      fontSize: 25,
    },
    input: {
      height: 40,
      margin: 12,
      borderWidth: 1,
    },
    bigInput: {
      height: 120,
      margin: 12,
      borderWidth: 1,
    },
    genderBlockContainer: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      marginHorizontal: 20,
      alignItems: 'center',
    },
    genderText: {
      fontSize: 14,
    },
    genderBlockSelected: {
      width: '40%',
      height: 40,
      padding: 10,
      backgroundColor: '#bdc3c7',
      margin: 10,
      borderRadius: 5,
      alignItems: 'center',
      justifyContent: 'center',
    },
    genderBlock: {
      height: 40,
      padding: 10,
      width: '40%',
      backgroundColor: '#ecf0f1',
      borderRadius: 5,
      alignItems: 'center',
      justifyContent: 'center',
    },
  });
}

export default editMyProfile;
