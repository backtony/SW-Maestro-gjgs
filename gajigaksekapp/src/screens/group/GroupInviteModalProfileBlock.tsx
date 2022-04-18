import * as React from 'react';
import {Text, View, Image, TouchableOpacity, StyleSheet} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

export default class GroupInviteModalProfileBlock extends React.Component<
  {
    profile: any;
    memberEdit: boolean;
    memberAdd: boolean;
  },
  {}
> {
  render() {
    return (
      <View style={this.styles.container}>
        <View style={{flexDirection: 'row', alignItems: 'center'}}>
          <TouchableOpacity
            style={this.props.memberEdit ? {} : {display: 'none'}}>
            <Icon
              name="remove-circle-sharp"
              size={24}
              color={'#ff4f4f'}
              style={{marginRight: 16}}
            />
          </TouchableOpacity>
          <Image
            style={this.styles.img}
            source={{
              uri: 'https://cdn.icon-icons.com/icons2/2506/PNG/512/user_icon_150670.png',
            }}
          />
          <View>
            <View style={{flexDirection: 'row'}}>
              <Text style={this.styles.text1}>
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
          </View>
        </View>
        {this.props.memberAdd && (
          <TouchableOpacity>
            <Icon name="add-circle-sharp" size={24} color={'#4f6cff'} />
          </TouchableOpacity>
        )}
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {
      width: '100%',
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'space-between',
      paddingVertical: 17,
    },
    img: {
      width: 44,
      height: 44,
      borderRadius: 44,
      borderWidth: 1,
      borderColor: '#f5f5f7',
      borderStyle: 'solid',
      marginRight: 16,
    },
    text1: {
      fontSize: 16,
      fontFamily: 'NotoSansCJKkr-Regular',
      lineHeight: 18,
    },
    text2: {
      fontSize: 14,
      fontFamily: 'NotoSansCJKkr-Regular',
      lineHeight: 16,
      paddingTop: 4,
      color: '#8e8e8f',
    },
  });
}
