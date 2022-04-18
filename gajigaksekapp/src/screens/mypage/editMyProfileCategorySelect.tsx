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
import {STATUSBAR_HEIGHT} from '../../utils/commonParam';
import Icon from 'react-native-vector-icons/Ionicons';
import {Button} from 'react-native-elements';
import FirstSubCategoryButton from './firstLogin/FirstSubCategoryButton';
import Category from '../../utils/Category';
import FirstSubCategoryContainer from './firstLogin/FirstSubCategoryContainer';
import MypageApiController from '../../services/apis/MypageApiController';

export default class editMyProfileCategorySelect extends React.Component<
  {selectedSubIdList: number[]; setSubCategory: (idList: number[]) => void},
  {subCategoryIdList: number[]}
> {
  constructor() {
    super();
    this.state = {subCategoryIdList: []};
  }

  componentDidMount() {
    this.setState({
      subCategoryIdList: this.props.route.params.selectedSubIdList,
    });
  }

  private async putMypageCategory(idList: number[]) {
    console.log('idList: ', idList);
    try {
      await MypageApiController.putCategory({categoryIdList: idList});
      this.props.route.params.setSubCategory(this.state.subCategoryIdList);
      this.props.navigation.goBack();
    } catch (e) {
      console.error(e);
    }
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
              관심분야 변경하기
            </Text>
          </View>
        </View>
        <ScrollView style={{padding: 20}}>
          {Category.getCategoryList().map(item => (
            <FirstSubCategoryContainer
              mainCategory={item}
              subCategoryIdList={this.state.subCategoryIdList}
              updateSubCategoryList={(subCategoryIdList: number[]) => {
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
            {this.state.subCategoryIdList &&
              this.state.subCategoryIdList.map(id => (
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
                this.putMypageCategory(this.state.subCategoryIdList);
              }}>
              <Text
                style={{
                  // width: 93,
                  // height: 20,
                  fontFamily: 'NotoSansCJKkr-Bold',
                  fontSize: 16,
                  color: '#FFF',
                }}>
                수정하기
              </Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    );
  }
}
