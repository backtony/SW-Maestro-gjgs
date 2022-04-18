import * as React from 'react';
import {Text, View, Image, TouchableOpacity, StyleSheet} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import TeamManageApiController from '../../services/apis/TeamManageApiController';

export default class GroupApplyListModalProfileBlock extends React.Component<
  {
    applierId: number;
    teamId: number;
    profile: any;
    onClicked: () => void;
  },
  {}
> {
  private async postApplierConfirm() {
    try {
      await TeamManageApiController.postApplierConfirm({
        applierId: this.props.applierId,
        teamId: this.props.teamId,
      });
      this.props.onClicked();
    } catch (e) {
      console.error(e);
    }
  }

  private async deleteApplier() {
    try {
      await TeamManageApiController.deleteApplier({
        applierId: this.props.applierId,
        teamId: this.props.teamId,
      });
      this.props.onClicked();
    } catch (e) {
      console.error(e);
    }
  }
  render() {
    return (
      <TouchableOpacity style={this.styles.button} onPress={() => {}}>
        <View style={{flexDirection: 'row', alignItems: 'center'}}>
          <Image
            style={this.styles.img}
            source={{
              uri: this.props.profile.thumbnailImageUrl,
            }}
          />
          <View style={{justifyContent: 'center'}}>
            <View style={{flexDirection: 'row'}}>
              <Text style={this.styles.text}>
                {this.props.profile.nickname}
              </Text>
              {this.props.profile.gender === 'M' && (
                <Icon
                  name="male-sharp"
                  size={14}
                  color={'#4f6cff'}
                  style={{marginLeft: 6}}
                />
              )}
              {this.props.profile.gender === 'F' && (
                <Icon
                  name="female-sharp"
                  size={14}
                  color={'#ff4f4f'}
                  style={{marginLeft: 6}}
                />
              )}
            </View>
            <Text style={this.styles.text2}>
              {`${this.props.profile.age}세`}
            </Text>
            <View style={this.styles.wrapper}>
              <TouchableOpacity
                style={this.styles.button2}
                onPress={() => {
                  this.postApplierConfirm();
                }}>
                <Text style={this.styles.text3}>수락</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={this.styles.button3}
                onPress={() => {
                  this.deleteApplier();
                }}>
                <Text style={this.styles.text4}>거절</Text>
              </TouchableOpacity>
            </View>
          </View>
        </View>
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    text4: {
      fontSize: 14,
      fontFamily: 'NotoSansCJKkr-Regular',
      lineHeight: 16,
      paddingTop: 4,
      color: '#1d1d1f',
    },
    button3: {
      backgroundColor: '#ececec',
      width: 103,
      height: 32,
      borderRadius: 6,
      alignItems: 'center',
      justifyContent: 'center',
    },
    text3: {
      fontSize: 14,
      fontFamily: 'NotoSansCJKkr-Regular',
      lineHeight: 16,
      paddingTop: 4,
      color: '#fff',
    },
    button2: {
      backgroundColor: '#4f6cff',
      width: 103,
      height: 32,
      borderRadius: 6,
      alignItems: 'center',
      justifyContent: 'center',
      marginRight: 10,
    },
    wrapper: {
      flexDirection: 'row',
    },
    text2: {
      fontSize: 14,
      fontFamily: 'NotoSansCJKkr-Regular',
      lineHeight: 16,
      paddingTop: 4,
      color: '#8e8e8f',
    },
    text: {
      fontSize: 16,
      fontFamily: 'NotoSansCJKkr-Regular',
      lineHeight: 18,
    },
    img: {
      width: 88,
      height: 88,
      borderRadius: 88,
      borderWidth: 1,
      borderColor: '#f5f5f7',
      borderStyle: 'solid',
      marginRight: 10,
    },
    button: {
      backgroundColor: '#FAFAFB',
      width: '100%',
      borderRadius: 6,
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'space-between',
      paddingVertical: 17,
      paddingHorizontal: 20,
    },
  });
}
