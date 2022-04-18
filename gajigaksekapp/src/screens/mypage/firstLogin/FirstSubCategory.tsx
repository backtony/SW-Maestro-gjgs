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
  SafeAreaView,
  Alert,
} from 'react-native';
import {STATUSBAR_HEIGHT} from '../../../utils/commonParam';
import Icon from 'react-native-vector-icons/Ionicons';
import {Button} from 'react-native-elements';
import FirstSubCategoryButton from './FirstSubCategoryButton';
import Category from '../../../utils/Category';
import FirstSubCategoryContainer from './FirstSubCategoryContainer';
import UserDC from '../../../services/login/UserDC';
import axios from 'axios';
import LoginApiController from '../../../services/apis/LoginApiController';
import AsyncStorage from '@react-native-async-storage/async-storage';

export default class FirstSubCategory extends React.Component<
  {},
  {subCategoryIdList: number[]}
> {
  constructor() {
    super();
    this.state = {subCategoryIdList: []};
  }

  componentDidMount() {
    console.log(this.props.route.params);
    if (!UserDC.getUser()) {
      alert('카카오 로그인을 다시 해주세요.');
    }
  }

  private async firstLoginPostApi() {
    const {age, id, imageFileUrl, name, nickname, phone, sex, zoneId} =
      this.props.route.params;
    if (name.length < 2) {
      alert('이름을 2글자 이상 입력해주세요.');
      return;
    }

    if (nickname.length < 2) {
      alert('닉네임을 2글자 이상 입력해주세요.');
      return;
    }

    if (phone.length < 10 || phone.length > 11) {
      alert('핸드폰번호는 10자리 에서 11자리 사이에 입력해주세요.');
      return;
    }

    if (this.state.subCategoryIdList.length === 0) {
      alert('관심분야를 적어도 1개이상 골라주세요.');
      return;
    }

    console.log('FirstSubCategory imageFileUrl: ', imageFileUrl);

    try {
      const res = await LoginApiController.firstLogin({
        ...this.props.route.params,
        categoryIdList: this.state.subCategoryIdList,
      });

      UserDC.setData(res?.data);
      AsyncStorage.setItem('user', JSON.stringify(UserDC.getUser()));
      console.log('USER: ', UserDC.getUser());
      this.props.navigation.navigate('mypage');
    } catch (e) {
      console.error(e);
    }

    // try {
    //   const res = await axios.post(
    //     'http://15.165.18.93:8080/api/v1/login/first',
    //     {
    //       id: id,
    //       imageFileURL: imageFileUrl,
    //       name: name,
    //       phone: phone,
    //       nickname: nickname,
    //       age: age,
    //       sex: sex,
    //       zoneId: zoneId,
    //       categoryId: this.state.subCategoryIdList,
    //     },
    //   );
    //   console.log('11: ', res);
    // } catch (e) {
    //   alert('에러가 발생했습니다. 다시 정보를 입력후 시도해주세요.');
    //   console.error(e);
    // }

    // this.user.setData(res.data.data);
  }

  render() {
    return (
      <View style={{backgroundColor: '#FFF', flex: 1}}>
        <View
          style={{
            marginTop: STATUSBAR_HEIGHT,
            height: 60,
            width: '100%',
            // flexDirection: 'row',
            justifyContent: 'space-between',
            alignItems: 'flex-start',
          }}>
          <View style={{flexDirection: 'row', alignItems: 'center'}}>
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
              관심분야 선택하기
            </Text>
          </View>
          <Text
            style={{
              // width: 93,
              // height: 20,
              fontFamily: 'NotoSansCJKkr-Bold',
              fontSize: 10,
              color: '#1d1d1f',
              marginLeft: 30,
            }}>
            최소 1개 최대 3개까지 선택 가능합니다.
          </Text>
        </View>
        <ScrollView style={{padding: 20}}>
          {Category.getCategoryList().map(item => (
            <FirstSubCategoryContainer
              mainCategory={item}
              subCategoryIdList={this.state.subCategoryIdList}
              updateSubCategoryList={(subCategoryIdList: number[]) => {
                console.log('updated categoryId list : ', subCategoryIdList);
                this.setState({subCategoryIdList});
              }}
            />
          ))}
          <View style={{height: 115}} />
        </ScrollView>
        <View
          style={{
            position: 'absolute',
            bottom: 0,
            right: 0,
            width: '100%',
            height: 108,
            backgroundColor: '#FFF',
            borderTopWidth: 1,
            borderTopColor: '#f5f5f7',
          }}>
          <ScrollView
            horizontal={true}
            style={{
              height: 48,
              flexDirection: 'row',
              padding: 10,
              paddingLeft: 0,
              marginLeft: 20,
            }}>
            {this.state.subCategoryIdList.map(id => (
              <View
                style={{
                  flexDirection: 'row',
                  alignItems: 'center',
                  backgroundColor: '#f5f7ff',
                  paddingHorizontal: 8,
                  borderRadius: 6,
                  heigth: 28,
                  marginRight: 5,
                }}>
                <TouchableOpacity
                  style={{marginRight: 5}}
                  onPress={() => {
                    let updatedList = [...this.state.subCategoryIdList, id];

                    if (this.state.subCategoryIdList.includes(id)) {
                      updatedList = updatedList.filter(value => value !== id);
                      this.setState({subCategoryIdList: updatedList});
                      return;
                    }

                    this.setState({subCategoryIdList: updatedList});
                  }}>
                  <Icon name="close-circle" size={16} color="#4f6cff" />
                </TouchableOpacity>
                <Text
                  style={{
                    // width: 93,
                    // height: 20,
                    // fontFamily: 'NotoSansCJKkr-Bold',
                    fontSize: 14,
                    color: '#4f6cff',
                  }}>
                  {Category.getCategory(id)[1]}
                </Text>
              </View>
            ))}
          </ScrollView>

          <View
            style={{
              height: 60,
              paddingHorizontal: 20,
            }}>
            <TouchableOpacity
              style={{
                backgroundColor: '#4f6cff',
                height: 44,
                alignItems: 'center',
                justifyContent: 'center',
                borderRadius: 6,
              }}
              onPress={() => {
                this.firstLoginPostApi();
              }}>
              <Text
                style={{
                  // width: 93,
                  // height: 20,
                  fontFamily: 'NotoSansCJKkr-Bold',
                  fontSize: 16,
                  color: '#FFF',
                }}>
                회원가입
              </Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    );
  }
}
