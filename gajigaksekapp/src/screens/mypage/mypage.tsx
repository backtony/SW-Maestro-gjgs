import * as React from 'react';
import {
  StyleSheet,
  Image,
  Text,
  View,
  ScrollView,
  TouchableOpacity,
  Alert,
} from 'react-native';
import {Button} from 'react-native-elements';
import UserDC, {UserDCListener} from '../../services/login/UserDC';
import SwitchSelector from 'react-native-switch-selector';
import Icon from 'react-native-vector-icons/Ionicons';
import {STATUSBAR_HEIGHT} from '../../utils/commonParam';
import User from '../../services/login/User';
import axios from 'axios';
import LoginApiController from '../../services/apis/LoginApiController';
import MypageApiController from '../../services/apis/MypageApiController';
import Category from '../../utils/Category';

class mypage
  extends React.Component<
    {},
    {
      isLogin: boolean;
      nickname: string;
      directorSelected: boolean;
      imageFileUrl: string;
      categoryIdList: number[];
      memberId: number;
      totalReward: number;
    }
  >
  implements UserDCListener
{
  constructor() {
    super();
    UserDC.addListener(this);
    this.state = {
      isLogin: !UserDC.isLogout(),
      nickname: 'default',
      directorSelected: false,
      imageFileUrl:
        'https://cdn.icon-icons.com/icons2/2506/PNG/512/user_icon_150670.png',
      categoryIdList: [],
      totalReward: 0,
    };
  }

  public userLoggedIn(user: User): void {
    this.setState({isLogin: true, nickname: user.nickname});
    if (!user.tokenDto) {
      this.props.navigation.navigate('firstLogin');
    }
    if (user.imageFileUrl) {
      console.log('imageFileUrl: ', user.imageFileUrl);
      this.setState({imageFileUrl: user.imageFileUrl});
    }
    if (user.memberId) {
      this.setState({memberId: user.memberId});
    }
    this.getMypageApi();
  }

  public userLoggedOut(): void {
    this.setState({
      isLogin: false,
      imageFileUrl:
        'https://cdn.icon-icons.com/icons2/2506/PNG/512/user_icon_150670.png',
    });
  }

  componentDidMount() {
    const user = UserDC.getUser();
    if (user) {
      this.setState({nickname: user.nickname});
    }

    if (user && user.imageFileUrl) {
      this.setState({imageFileUrl: user.imageFileUrl});
    }

    if (user && user.memberId) {
      this.setState({memberId: user.memberId});
    }

    this.getMypageApi();
  }

  private async getMypageApi() {
    try {
      const res = await MypageApiController.getMypage({});
      console.log('getMyPage res1: ', res);
      this.setState(
        {
          categoryIdList: res?.data.memberCategoryIdList,
          imageFileUrl: res?.data.imageFileUrl,
          totalReward: res?.data.totalReward,
        },
        () => console.log('211007: ', this.state),
      );
    } catch (e) {
      console.error(e);
    }
  }

  private async testReissue() {
    const user = await UserDC.getUser();
    const newTokenDto = await LoginApiController.getNewAccessToken(
      user.tokenDto,
    );
    user.tokenDto = newTokenDto;
  }

  render() {
    return (
      <View
        style={{
          flex: 1,
          justifyContent: 'center',
          alignItems: 'center',
          backgroundColor: '#FFF',
        }}>
        {/* Header */}
        <View
          style={{
            marginTop: STATUSBAR_HEIGHT,
            height: 60,
            width: '100%',
            flexDirection: 'row',
            justifyContent: 'space-between',
            alignItems: 'center',
            paddingHorizontal: 15,
          }}>
          <Text
            style={{
              // width: 93,
              // height: 20,
              fontFamily: 'NotoSansCJKkr-Bold',
              fontSize: 20,
              color: '#1d1d1f',
            }}>
            마이페이지
          </Text>
          <View
            style={{
              flexDirection: 'row',
            }}>
            <Button
              icon={<Icon name="notifications" size={24} color="black" />}
              buttonStyle={{backgroundColor: 'white'}}
              onPress={() => this.props.navigation.navigate('alarmPage')}
              // onPress={() => this.testReissue()}
            />
            <Button
              icon={<Icon name="settings-sharp" size={24} color="black" />}
              buttonStyle={{backgroundColor: 'white'}}
              onPress={() =>
                this.props.navigation.navigate('settingPage', {
                  nickname: this.state.nickname,
                  refreshParent: () => {
                    this.getMypageApi();
                  },
                })
              }
            />
          </View>
        </View>
        <ScrollView style={this.styles.scrollview}>
          {this.state.isLogin && (
            <View
              style={{
                marginTop: 10,
                alignItems: 'center',
              }}>
              <SwitchSelector
                initial={0}
                onPress={() => {
                  this.setState({
                    directorSelected: !this.state.directorSelected,
                  });
                }}
                textColor={'#4f6cff'}
                selectedColor={'white'}
                buttonColor={'#4f6cff'}
                borderColor={'#FFF'}
                fontSize={14}
                hasPadding
                bold={true}
                options={[
                  {label: '수강생', value: 'notDirector'}, //images.masculino = require('./path_to/assets/img/masculino.png')
                  {label: '디렉터', value: 'director'}, //images.feminino = require('./path_to/assets/img/feminino.png')
                ]}
                height={37}
                textStyle={{fontFamily: 'NotoSansCJKkr-Bold'}}
                backgroundColor={'#f5f7ff'}
                style={{
                  width: 200,
                }}
                testID="gender-switch-selector"
                accessibilityLabel="gender-switch-selector"
              />
            </View>
          )}
          <View style={this.styles.profileItem}>
            {!this.state.isLogin && (
              <View style={this.styles.avatar}>
                <Icon
                  name="md-person-sharp"
                  size={24}
                  color="#8e8e8f"
                  style={{}}
                />
              </View>
            )}
            {this.state.isLogin && (
              <Image
                style={this.styles.avatar}
                source={{
                  uri: this.state.imageFileUrl,
                }}
              />
            )}
            <TouchableOpacity
              style={[
                this.styles.kakaoBtnContainer,
                !this.state.isLogin ? {} : {display: 'none'},
              ]}
              onPress={async () => {
                await UserDC.signInWithKakao();
              }}>
              <View style={{flexDirection: 'row', alignItems: 'center'}}>
                <Text
                  style={{
                    fontFamily: 'NotoSansCJKkr-Bold',
                    fontSize: 20,
                    color: '#1d1d1f',
                  }}>
                  로그인 하기
                </Text>
                <Icon name="chevron-forward" size={25} color="black" />
              </View>
            </TouchableOpacity>
            <TouchableOpacity
              style={[
                {
                  // backgroundColor: 'brown',
                  width: '100%',
                  height: '100%',
                },
                !this.state.isLogin ? {display: 'none'} : {},
              ]}
              onPress={() => {
                if (!this.state.directorSelected) {
                  // this.props.navigation.navigate('editProfile', {
                  //   nickname: this.state.nickname,
                  //   gender: 'F',
                  //   age: 25,
                  // });
                  this.props.navigation.navigate('profile', {
                    memberId: this.state.memberId,
                  });
                } else {
                  // this.props.navigation.navigate('editDirectorProfile', {
                  //   nickname: this.state.nickname,
                  //   gender: 'F',
                  //   age: 25,
                  // });
                }
              }}>
              <View style={{justifyContent: 'center'}}>
                <Text
                  style={{
                    fontSize: 20,
                    fontFamily: 'NotoSansCJKkr-Bold',
                    lineHeight: 21,
                    paddingTop: 13,
                  }}>
                  {this.state.nickname}
                </Text>
                {this.state.categoryIdList && (
                  <Text
                    style={{
                      fontSize: 14,
                      fontFamily: 'NotoSansCJKkr-Regular',
                      lineHeight: 15,
                      paddingTop: 4,
                      color: '#8e8e8f',
                      width: 250,
                    }}
                    numberOfLines={1}>
                    {this.state.categoryIdList
                      .map(id => `#${Category.getCategory(id)[1]}`)
                      .join(' ')}
                  </Text>
                )}
              </View>
            </TouchableOpacity>
          </View>
          {this.state.isLogin && !this.state.directorSelected && (
            <View style={this.styles.longButtonContainer}>
              <TouchableOpacity
                style={{
                  flexDirection: 'row',
                  backgroundColor: '#f5f5f7',
                  marginHorizontal: 20,
                  borderRadius: 6,
                  height: 64,
                  justifyContent: 'space-between',
                  alignItems: 'center',
                }}
                onPress={() => {
                  this.props.navigation.navigate('reward', {
                    totalReward: this.state.totalReward,
                  });
                }}>
                <View
                  style={{
                    marginLeft: 20,
                    marginTop: 9,
                    flexDirection: 'row',
                    justifyContent: 'space-between',
                    flex: 1,
                  }}>
                  <View style={{flexDirection: 'row', marginBottom: 4}}>
                    <Image
                      style={{width: 28, height: 28}}
                      source={{
                        uri: 'https://cdn.zeplin.io/60f22448a6ebf90ffb53be83/assets/7BD9BFD9-0F76-4D13-8582-9C28A1412585.png',
                      }}
                    />
                    <Text
                      style={{
                        fontSize: 16,
                        marginLeft: 10,
                        fontFamily: 'NotoSansCJKkr-Bold',
                      }}>
                      리워드
                    </Text>
                  </View>
                  <Text
                    style={{
                      fontSize: 16,
                      fontFamily: 'NotoSansCJKkr-Bold',
                      marginBottom: 4,
                    }}>
                    {`${this.state.totalReward}원`}
                  </Text>
                </View>
                <Icon
                  name="chevron-forward"
                  size={25}
                  color="#8e8e8f"
                  style={{paddingRight: 12}}
                />
              </TouchableOpacity>

              <TouchableOpacity
                style={{
                  flexDirection: 'row',
                  backgroundColor: '#f5f5f7',
                  marginHorizontal: 20,
                  borderRadius: 6,
                  height: 64,
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  marginTop: 10,
                }}
                onPress={() => {
                  this.props.navigation.navigate('coupon');
                }}>
                <View
                  style={{
                    marginLeft: 20,
                    marginTop: 9,
                    flexDirection: 'row',
                    justifyContent: 'space-between',
                    flex: 1,
                  }}>
                  <View style={{flexDirection: 'row', marginBottom: 4}}>
                    <Image
                      style={{width: 28, height: 28}}
                      source={{
                        uri: 'https://cdn-icons-png.flaticon.com/512/815/815252.png',
                      }}
                    />
                    <Text
                      style={{
                        fontSize: 16,
                        marginLeft: 10,
                        fontFamily: 'NotoSansCJKkr-Bold',
                      }}>
                      쿠폰
                    </Text>
                  </View>
                  {/* <Text
                    style={{
                      fontSize: 16,
                      fontFamily: 'NotoSansCJKkr-Bold',
                      marginBottom: 4,
                    }}>
                    {`${this.state.totalReward}원`}
                  </Text> */}
                </View>
                <Icon
                  name="chevron-forward"
                  size={25}
                  color="#8e8e8f"
                  style={{paddingRight: 12}}
                />
              </TouchableOpacity>
              <TouchableOpacity
                style={{
                  flexDirection: 'row',
                  backgroundColor: '#f5f5f7',
                  marginHorizontal: 20,
                  borderRadius: 6,
                  height: 64,
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  marginTop: 10,
                }}
                onPress={() => {
                  this.props.navigation.navigate('bulletinList');
                }}>
                <View style={{marginLeft: 20, marginTop: 9}}>
                  <Text
                    style={{
                      fontSize: 16,
                      lineHeight: 19,
                      fontFamily: 'NotoSansCJKkr-Bold',
                      marginBottom: 4,
                    }}>
                    나의 게시글
                  </Text>
                </View>
                <Icon
                  name="chevron-forward"
                  size={25}
                  color="#8e8e8f"
                  style={{paddingRight: 12}}
                />
              </TouchableOpacity>
              <TouchableOpacity
                style={{
                  flexDirection: 'row',
                  backgroundColor: '#f5f5f7',
                  marginHorizontal: 20,
                  borderRadius: 6,
                  height: 64,
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  marginTop: 10,
                }}
                onPress={() => {
                  this.props.navigation.navigate('myclass');
                }}>
                <View style={{marginLeft: 20, marginTop: 9}}>
                  <Text
                    style={{
                      fontSize: 16,
                      lineHeight: 19,
                      fontFamily: 'NotoSansCJKkr-Bold',
                      marginBottom: 4,
                    }}>
                    나의 클래스
                  </Text>
                </View>
                <Icon
                  name="chevron-forward"
                  size={25}
                  color="#8e8e8f"
                  style={{paddingRight: 12}}
                />
              </TouchableOpacity>
              <TouchableOpacity
                style={{
                  flexDirection: 'row',
                  backgroundColor: '#f5f5f7',
                  marginHorizontal: 20,
                  borderRadius: 6,
                  height: 64,
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  marginTop: 10,
                }}
                onPress={() => {
                  this.props.navigation.navigate('questionList');
                }}>
                <View style={{marginLeft: 20, marginTop: 9}}>
                  <Text
                    style={{
                      fontSize: 16,
                      lineHeight: 19,
                      fontFamily: 'NotoSansCJKkr-Bold',
                      marginBottom: 4,
                    }}>
                    나의 문의
                  </Text>
                </View>
                <Icon
                  name="chevron-forward"
                  size={25}
                  color="#8e8e8f"
                  style={{paddingRight: 12}}
                />
              </TouchableOpacity>

              <TouchableOpacity
                style={{
                  flexDirection: 'row',
                  backgroundColor: '#f5f5f7',
                  marginHorizontal: 20,
                  borderRadius: 6,
                  height: 64,
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  marginTop: 10,
                }}
                onPress={() => {
                  this.props.navigation.navigate('myreview');
                }}>
                <View style={{marginLeft: 20, marginTop: 9}}>
                  <Text
                    style={{
                      fontSize: 16,
                      lineHeight: 19,
                      fontFamily: 'NotoSansCJKkr-Bold',
                      marginBottom: 4,
                    }}>
                    나의 리뷰
                  </Text>
                </View>
                <Icon
                  name="chevron-forward"
                  size={25}
                  color="#8e8e8f"
                  style={{paddingRight: 12}}
                />
              </TouchableOpacity>

              <TouchableOpacity
                style={{
                  flexDirection: 'row',
                  backgroundColor: '#f5f5f7',
                  marginHorizontal: 20,
                  borderRadius: 6,
                  height: 64,
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  marginTop: 10,
                }}
                onPress={() => {
                  this.props.navigation.navigate('noticeList');
                }}>
                <View style={{marginLeft: 20, marginTop: 9}}>
                  <Text
                    style={{
                      fontSize: 16,
                      lineHeight: 19,
                      fontFamily: 'NotoSansCJKkr-Bold',
                      marginBottom: 4,
                    }}>
                    공지사항
                  </Text>
                </View>
                <Icon
                  name="chevron-forward"
                  size={25}
                  color="#8e8e8f"
                  style={{paddingRight: 12}}
                />
              </TouchableOpacity>

              <TouchableOpacity
                style={{
                  flexDirection: 'row',
                  backgroundColor: '#f5f5f7',
                  marginHorizontal: 20,
                  borderRadius: 6,
                  height: 64,
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  marginTop: 10,
                }}
                onPress={() => {
                  // this.props.navigation.navigate('questionList');
                }}>
                <View style={{marginLeft: 20, marginTop: 9}}>
                  <Text
                    style={{
                      fontSize: 16,
                      lineHeight: 19,
                      fontFamily: 'NotoSansCJKkr-Bold',
                      marginBottom: 4,
                    }}>
                    자주 묻는 질문
                  </Text>
                </View>
                <Icon
                  name="chevron-forward"
                  size={25}
                  color="#8e8e8f"
                  style={{paddingRight: 12}}
                />
              </TouchableOpacity>
              <TouchableOpacity
                style={{
                  flexDirection: 'row',
                  backgroundColor: '#f5f5f7',
                  marginHorizontal: 20,
                  borderRadius: 6,
                  height: 64,
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  marginTop: 10,
                }}
                onPress={() => {
                  // this.props.navigation.navigate('questionList');
                }}>
                <View style={{marginLeft: 20, marginTop: 9}}>
                  <Text
                    style={{
                      fontSize: 16,
                      lineHeight: 19,
                      fontFamily: 'NotoSansCJKkr-Bold',
                      marginBottom: 4,
                    }}>
                    고객센터
                  </Text>
                </View>
                <Icon
                  name="chevron-forward"
                  size={25}
                  color="#8e8e8f"
                  style={{paddingRight: 12}}
                />
              </TouchableOpacity>
            </View>
          )}
          {this.state.isLogin && this.state.directorSelected && (
            <View style={this.styles.longButtonContainer}>
              <TouchableOpacity
                style={{
                  flexDirection: 'row',
                  backgroundColor: '#f5f5f7',
                  marginHorizontal: 20,
                  borderRadius: 6,
                  height: 64,
                  justifyContent: 'space-between',
                  alignItems: 'center',
                }}
                onPress={() => Alert.alert('클래스 등록은 웹으로 해주세요.')}>
                <View style={{marginLeft: 20, marginTop: 4}}>
                  <Text
                    style={{
                      fontSize: 16,
                      lineHeight: 19,
                      fontFamily: 'NotoSansCJKkr-Bold',
                    }}>
                    클래스 등록
                  </Text>
                </View>
                <Icon
                  name="chevron-forward"
                  size={25}
                  color="#8e8e8f"
                  style={{paddingRight: 12}}
                />
              </TouchableOpacity>
              <TouchableOpacity
                style={{
                  flexDirection: 'row',
                  backgroundColor: '#f5f5f7',
                  marginHorizontal: 20,
                  borderRadius: 6,
                  height: 64,
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  marginTop: 10,
                }}
                onPress={() => Alert.alert('클래스 관리는 웹으로 해주세요.')}>
                <View style={{marginLeft: 20, marginTop: 4}}>
                  <Text
                    style={{
                      fontSize: 16,
                      lineHeight: 19,
                      fontFamily: 'NotoSansCJKkr-Bold',
                    }}>
                    클래스 관리
                  </Text>
                </View>
                <Icon
                  name="chevron-forward"
                  size={25}
                  color="#8e8e8f"
                  style={{paddingRight: 12}}
                />
              </TouchableOpacity>
            </View>
          )}
        </ScrollView>

        <View style={this.styles.bottomButtonContainer}>
          <View>
            <Button
              buttonStyle={this.styles.bottomButton}
              titleStyle={{
                fontSize: 14,
                color: '#1d1d1f',
                fontFamily: 'NotoSansCJKkr-Regular',
                paddingLeft: 25,
              }}
              onPress={() => {
                // this.props.navigation.navigate('firstSubCategory', {
                //   // nickname: this.state.nickname,
                // });
              }}
              title="상담원 연결"
            />
            <Image
              style={{
                width: 24,
                height: 24,
                position: 'absolute',
                left: 29,
                top: 8,
              }}
              source={require('gajigaksekapp/src/asset/iconImage/support_agent.png')}
            />
          </View>
          <View>
            <Button
              buttonStyle={[
                this.styles.bottomButton,
                this.styles.bottomKakaoButton,
              ]}
              titleStyle={{
                fontSize: 14,
                color: '#1d1d1f',
                fontFamily: 'NotoSansCJKkr-Regular',
                paddingLeft: 29,
              }}
              title="카카오톡 문의"
              onPress={() => {
                // this.props.navigation.navigate('firstLogin', {
                //   nickname: this.state.nickname,
                // });
              }}
              onLongPress={() => {
                this.props.navigation.navigate('fakelogin');
              }}
            />
            <Image
              style={{
                width: 21,
                height: 20,
                position: 'absolute',
                left: 23,
                top: 10,
              }}
              source={require('gajigaksekapp/src/asset/iconImage/group.png')}
            />
          </View>
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    scrollview: {
      width: '100%',
    },
    avatar: {
      width: 60,
      height: 60,
      borderRadius: 63,
      borderWidth: 1,
      borderColor: '#f5f5f7',
      borderStyle: 'solid',
      // marginBottom: 10,
      // alignSelf: 'center',
      // position: 'absolute',
      // marginTop: 130,
      marginHorizontal: 20,
      backgroundColor: '#f5f5f7',
      alignItems: 'center',
      justifyContent: 'center',
    },
    itemText: {
      padding: 10,
      fontSize: 18,
      height: 44,
    },
    item: {
      backgroundColor: 'brown',
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'flex-start',
      width: '100%',
    },
    profileItem: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'flex-start',
      width: '100%',
      marginTop: 20,
    },
    kakaoBtnContainer: {
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'flex-start',
      width: '100%',
      height: '100%',
    },
    test: {
      // backgroundColor: 'black',
      width: '100%',
    },
    bottomButtonContainer: {
      flexDirection: 'row',
      justifyContent: 'space-around',
      width: '100%',
      padding: 20,
    },
    button: {
      width: 150,
    },
    bottomButton: {
      width: 155,
      height: 40,
      borderRadius: 6,
      backgroundColor: '#f5f5f7',
      padding: 1 * 0.99,
    },
    bottomKakaoButton: {
      backgroundColor: '#fee500',
    },
    longButtonContainer: {
      width: '100%',
      paddingTop: 40,
    },
    longButton: {
      width: '100%',
      marginHorizontal: 20,
    },
    imageBlock: {
      width: 200,
      height: 50,
    },
  });
}

export default mypage;
