import React from 'react';
import {StyleSheet, Text, View, Image, ScrollView} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';
import {STATUSBAR_HEIGHT} from '../../utils/commonParam';
import MypageApiController from '../../services/apis/MypageApiController';
import Zone from '../../utils/Zone';
import Category from '../../utils/Category';

export default class Profile extends React.Component<{}, {profile: any}> {
  constructor() {
    super();
    this.state = {
      profile: {
        age: 27,
        sex: 'F',
        zoneId: 7,
        profileText: '이것은 프로필 텍스트입니다.',
        name: '이지원',
        nickname: '이지원12',
        imageFileUrl: 'https://bootdey.com/img/Content/avatar/avatar6.png',
        memberCategoryId: [1, 2],
      },
    };
  }

  componentDidMount() {
    this.getProfile(this.props.route.params.memberId);
  }

  private async getProfile(memberId: number) {
    try {
      const res = await MypageApiController.getProfile({memberId});
      console.log('res1: ', res);
      this.setState({profile: res.data});
    } catch (e) {
      console.error(e);
    }
  }
  render() {
    return (
      <ScrollView style={{backgroundColor: '#FFF'}}>
        <Image
          style={[this.styles.header]}
          source={require('gajigaksekapp/src/asset/profileHeaderImage.jpg')}
        />
        <View
          style={{
            marginTop: STATUSBAR_HEIGHT,
            height: 60,
            width: '100%',
            flexDirection: 'row',
            justifyContent: 'flex-start',
            alignItems: 'center',
            position: 'absolute',
            // backgroundColor: 'brown',
          }}>
          <Button
            icon={<Icon name="chevron-back" size={24} color="#FFF" />}
            buttonStyle={{backgroundColor: '(0, 0, 0, 0.5)'}}
            onPress={() => this.props.navigation.goBack()}
          />
          <Text
            style={{
              // width: 93,
              // height: 20,
              fontFamily: 'NotoSansCJKkr-Bold',
              fontSize: 20,
              color: '#FFF',
            }}>
            {this.state.profile.name}
          </Text>
        </View>
        <Image
          style={this.styles.avatar}
          source={{uri: this.state.profile.imageFileUrl}}
        />
        <View style={this.styles.body}>
          <View style={this.styles.bodyContent}>
            <Text style={this.styles.name}>{this.state.profile.nickname}</Text>
            <View style={this.styles.smallInfoBlock}>
              <View style={{flex: 1, alignItems: 'center'}}>
                <Text
                  style={{
                    fontFamily: 'NotoSansCJKkr-Regular',
                    fontSize: 12,
                    lineHeight: 14,
                    color: '#8e8e8f',
                    marginBottom: 2,
                  }}>
                  나이
                </Text>
                <Text
                  style={{
                    fontFamily: 'NotoSansCJKkr-Bold',
                    fontSize: 16,
                    lineHeight: 18,
                    color: '#1d1d1f',
                  }}>
                  {`${this.state.profile.age}세`}
                </Text>
              </View>
              <View style={{flex: 1, alignItems: 'center'}}>
                <Text
                  style={{
                    fontFamily: 'NotoSansCJKkr-Regular',
                    fontSize: 12,
                    lineHeight: 14,
                    color: '#8e8e8f',
                    marginBottom: 2,
                  }}>
                  성별
                </Text>
                <View style={{flexDirection: 'row', alignItems: 'center'}}>
                  <Text
                    style={{
                      fontFamily: 'NotoSansCJKkr-Bold',
                      fontSize: 16,
                      lineHeight: 18,
                      color: '#1d1d1f',
                    }}>
                    {this.state.profile.sex === 'F' ? '여성' : '남성'}
                  </Text>
                  {this.state.profile.sex === 'F' ? (
                    <Icon name="female" size={15} color="#ff4f4f" />
                  ) : (
                    <Icon name="male" size={15} color="#4f6cff" />
                  )}
                </View>
              </View>
              <View style={{flex: 1, alignItems: 'center'}}>
                <Text
                  style={{
                    fontFamily: 'NotoSansCJKkr-Regular',
                    fontSize: 12,
                    lineHeight: 14,
                    color: '#8e8e8f',
                    marginBottom: 2,
                  }}>
                  위치
                </Text>
                <Text
                  numberOfLines={1}
                  style={{
                    fontFamily: 'NotoSansCJKkr-Bold',
                    fontSize: 16,
                    lineHeight: 18,
                    color: '#1d1d1f',
                    width: '90%',
                  }}>
                  {Zone.getZone(this.state.profile.zoneId)[1]}
                </Text>
              </View>
            </View>

            <Text
              style={{
                fontFamily: 'NotoSansCJKkr-Bold',
                fontSize: 16,
                lineHeight: 18,
                color: '#1d1d1f',
                alignSelf: 'flex-start',
                marginLeft: 20,
                marginTop: 40,
              }}>
              관심분야
            </Text>
            <View
              style={{
                flexDirection: 'row',
                alignSelf: 'flex-start',
                marginLeft: 20,
                marginTop: 8,
              }}>
              {this.state.profile.categoryIdList &&
                this.state.profile.categoryIdList.map(id => (
                  <Text
                    key={id}
                    style={{
                      fontFamily: 'NotoSansCJKkr-Regular',
                      fontSize: 14,
                      lineHeight: 16,
                      color: '#1d1d1f',
                      marginRight: 3,
                    }}>
                    {`#${Category.getCategory(id)[1]}`}
                  </Text>
                ))}
            </View>
            <Text
              style={{
                fontFamily: 'NotoSansCJKkr-Bold',
                fontSize: 16,
                lineHeight: 18,
                color: '#1d1d1f',
                alignSelf: 'flex-start',
                marginLeft: 20,
                marginTop: 40,
              }}>
              소개
            </Text>
            <View style={{marginHorizontal: 20}}>
              <Text
                style={{
                  fontSize: 14,
                  lineHeight: 22,
                  fontFamily: 'NotoSansCJKkr-Regular',
                }}>
                {this.state.profile.profileText}
              </Text>
            </View>
          </View>
        </View>
      </ScrollView>
    );
  }

  private styles = StyleSheet.create({
    header: {
      backgroundColor: '#00BFFF',
      height: 140 + STATUSBAR_HEIGHT,
    },
    avatar: {
      width: 120,
      height: 120,
      borderRadius: 63,
      borderWidth: 2,
      borderColor: 'white',
      marginBottom: 10,
      alignSelf: 'center',
      position: 'absolute',
      marginTop: 80 + STATUSBAR_HEIGHT,
    },
    // name: {
    //   fontSize: 22,
    //   color: '#FFFFFF',
    //   fontWeight: '600',
    // },
    body: {
      marginTop: 40,
    },
    bodyContent: {
      flex: 1,
      alignItems: 'center',
      // padding: 30,
    },
    smallInfoBlock: {
      margin: 15,
      flexDirection: 'row',
      justifyContent: 'space-around',
      alignItems: 'center',
      height: 70,
      backgroundColor: '#fafafb',
      borderRadius: 10,
      width: 350,
    },
    smallInfo: {
      marginHorizontal: 30,
      marginVertical: 20,
      fontSize: 28,
      color: '#696969',
      fontWeight: '600',
    },
    name: {
      fontSize: 24,
      lineHeight: 28,
      color: '#1d1d1f',
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 35,
    },
    info: {
      fontSize: 16,
      color: '#00BFFF',
      marginTop: 10,
    },
    description: {
      fontSize: 16,
      color: '#696969',
      marginTop: 10,
      textAlign: 'center',
    },
    buttonContainer: {
      marginTop: 10,
      height: 45,
      flexDirection: 'row',
      justifyContent: 'center',
      alignItems: 'center',
      marginBottom: 20,
      width: 250,
      borderRadius: 30,
      backgroundColor: '#00BFFF',
    },
    hheader: {
      marginVertical: 20,
      fontSize: 28,
      fontWeight: '600',
      alignSelf: 'flex-start',
    },
    textBlock: {
      width: '100%',
      backgroundColor: '#bdbdbd',
      borderRadius: 5,
      flexDirection: 'row',
    },
    hashText: {
      margin: 10,
      fontSize: 18,
    },
  });
}
