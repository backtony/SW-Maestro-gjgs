import * as React from 'react';
import {StyleSheet} from 'react-native';
import {Text, TouchableOpacity} from 'react-native';

export default class GroupEditMemberButton extends React.Component<
  {title: string; clicked: boolean; onClick: () => void},
  {}
> {
  render() {
    return (
      <TouchableOpacity
        style={[
          this.styles.button,
          this.props.clicked ? {backgroundColor: '#4f6cff'} : {},
        ]}
        onPress={() => {
          this.props.onClick();
        }}>
        <Text
          style={[this.styles.text, this.props.clicked ? {color: '#FFF'} : {}]}>
          {this.props.title}
        </Text>
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    button: {
      backgroundColor: 'rgba(245, 245, 247, 0.6)',
      justifyContent: 'center',
      alignItems: 'center',
      //   width: 69,
      height: 32,
      borderRadius: 6,
      marginRight: 10,
      paddingHorizontal: 5,
    },
    text: {
      fontSize: 14,
      lineHeight: 16,
      fontFamily: 'NotoSansCJKkr-Regular',
      //   marginLeft: 20,
      marginTop: 3,
      //   marginBottom: 10,
      color: '#1d1d1f',
    },
  });
}
