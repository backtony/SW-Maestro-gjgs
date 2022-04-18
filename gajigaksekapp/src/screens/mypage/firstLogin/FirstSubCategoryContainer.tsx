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
import FirstSubCategoryButton from './FirstSubCategoryButton';
import Category from '../../../utils/Category';

export default class FirstSubCategoryContainer extends React.Component<
  {
    mainCategory: string;
    subCategoryIdList: number[];
    updateSubCategoryList: (subCategoryIdList: number[]) => void;
  },
  {}
> {
  render() {
    return (
      <View>
        <View
          style={{
            flexDirection: 'row',
            alignItems: 'center',
            //   marginLeft: 20,
          }}>
          {this.props.mainCategory === '액티비티' && (
            <Image
              style={{width: 16, height: 16}}
              source={require('gajigaksekapp/src/asset/iconImage/activity.png')}
            />
          )}
          {this.props.mainCategory === '쿠킹' && (
            <Image
              style={{width: 16, height: 16}}
              source={require('gajigaksekapp/src/asset/iconImage/cooking.png')}
            />
          )}
          {this.props.mainCategory === '뷰티/헬스' && (
            <Image
              style={{width: 16, height: 16}}
              source={require('gajigaksekapp/src/asset/iconImage/beautyHealth.png')}
            />
          )}
          {this.props.mainCategory === '댄스' && (
            <Image
              style={{width: 16, height: 16}}
              source={require('gajigaksekapp/src/asset/iconImage/dance.png')}
            />
          )}
          {this.props.mainCategory === '미술' && (
            <Image
              style={{width: 16, height: 16}}
              source={require('gajigaksekapp/src/asset/iconImage/art.png')}
            />
          )}
          {this.props.mainCategory === '수공예' && (
            <Image
              style={{width: 16, height: 16}}
              source={require('gajigaksekapp/src/asset/iconImage/handcraft.png')}
            />
          )}
          {this.props.mainCategory === '음악/예술' && (
            <Image
              style={{width: 16, height: 16}}
              source={require('gajigaksekapp/src/asset/iconImage/music.png')}
            />
          )}

          <Text
            style={{
              fontSize: 14,
              lineHeight: 16,
              fontFamily: 'NotoSansCJKkr-Bold',
              marginLeft: 3,
              marginTop: 7,
              //   marginBottom: 10,
            }}>
            {this.props.mainCategory}
          </Text>
        </View>
        <View
          style={{
            flexDirection: 'row',
            marginTop: 10,
            flexWrap: 'wrap',
            justifyContent: 'space-between',
          }}>
          {Category.getSubCategoryList(this.props.mainCategory).map(item => (
            <FirstSubCategoryButton
              title={item.sub}
              onPress={() => {
                let updatedList = [...this.props.subCategoryIdList, +item.id];

                if (this.props.subCategoryIdList.includes(+item.id)) {
                  updatedList = updatedList.filter(value => value !== +item.id);
                  this.props.updateSubCategoryList(updatedList);
                  return;
                }

                this.props.updateSubCategoryList(updatedList);
              }}
              clicked={this.props.subCategoryIdList.includes(+item.id)}
            />
          ))}
        </View>
      </View>
    );
  }
}
