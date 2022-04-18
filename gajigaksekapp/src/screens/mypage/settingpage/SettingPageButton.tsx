import React from 'react';
import {TouchableOpacity, Text} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

export default class SettingPageButton extends React.Component<
  {title: string; bottomBorder: boolean; onPress: () => void},
  {}
> {
  render() {
    return (
      <TouchableOpacity
        style={[
          {
            // backgroundColor: 'brown',
            flexDirection: 'row',
            justifyContent: 'space-between',
            alignItems: 'center',
            height: 50,
            marginLeft: 20,
            paddingRight: 15,
            borderBottomColor: '#f5f5f7',
          },
          this.props.bottomBorder
            ? {borderBottomWidth: 1}
            : {borderBottomWidth: 0},
        ]}
        onPress={() => {
          this.props.onPress();
        }}>
        <Text
          style={{
            color: '#1d1d1f',
            fontSize: 16,
            lineHeight: 18,
            fontFamily: 'NotoSansCJKkr-Regular',
          }}>
          {this.props.title}
        </Text>
        <Icon name="chevron-forward" size={18} color="#d2d2d2" />
      </TouchableOpacity>
    );
  }
}
