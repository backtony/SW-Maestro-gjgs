import * as React from 'react';
import {StyleSheet} from 'react-native';
import {Text, View, Image, TouchableOpacity} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import TeamManageApiController from '../services/apis/TeamManageApiController';

interface MemberBlockProps {
  profile: any;
  teamId: number;
  memberEdit: boolean;
  memberAdd: boolean;
  onDelete: () => void;
  navigation: any;
}

export default class MemberBlock extends React.Component<MemberBlockProps, {}> {
  private async deleteMember() {
    try {
      await TeamManageApiController.deleteMember({
        teamId: this.props.teamId,
        memberId: this.props.profile.memberId,
      });
      this.props.onDelete();
    } catch (e) {
      console.error(e);
    }
  }
  render() {
    return (
      <TouchableOpacity
        style={this.styles.button}
        onPress={() => {
          this.props.navigation.navigate('profile', {
            memberId: this.props.profile.memberId,
          });
        }}>
        <View style={this.styles.container}>
          <TouchableOpacity
            style={this.props.memberEdit ? {} : {display: 'none'}}
            onPress={() => {
              this.deleteMember();
            }}>
            <Icon
              name="remove-circle-sharp"
              size={24}
              color={'#ff4f4f'}
              style={{marginRight: 16}}
            />
          </TouchableOpacity>
          <Image
            style={this.styles.image}
            source={{
              uri: this.props.profile.image,
            }}
          />
          <View style={{justifyContent: 'center'}}>
            <View style={{flexDirection: 'row'}}>
              <Text style={this.styles.nickname}>
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
            {this.props.profile && (
              <Text style={this.styles.age}>
                {`${this.props.profile.age}세`}
              </Text>
            )}
            <Text numberOfLines={2} style={this.styles.info}>
              {this.props.profile.info}
            </Text>
          </View>
        </View>
        {this.props.memberAdd && (
          <Icon
            name="add-circle-sharp"
            size={24}
            color={'#ff4f4f'}
            style={{marginRight: 16}}
          />
        )}
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
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
    container: {flexDirection: 'row', alignItems: 'center'},
    image: {
      width: 60,
      height: 60,
      borderRadius: 63,
      borderWidth: 1,
      borderColor: '#f5f5f7',
      borderStyle: 'solid',
      marginRight: 10,
    },
    nickname: {
      fontSize: 16,
      fontFamily: 'NotoSansCJKkr-Regular',
      lineHeight: 18,
    },
    age: {
      fontSize: 14,
      fontFamily: 'NotoSansCJKkr-Regular',
      lineHeight: 16,
      paddingTop: 4,
      color: '#8e8e8f',
    },
    info: {
      fontSize: 12,
      fontFamily: 'NotoSansCJKkr-Regular',
      lineHeight: 14,
      paddingTop: 4,
      color: '#8e8e8f',
      width: '60%',
    },
  });
}
