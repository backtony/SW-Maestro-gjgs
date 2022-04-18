import React from 'react';
import {StyleSheet, Text, TouchableOpacity} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

interface HomeViewDirectorChangeButtonProps {
  onPress: () => void;
}

export default class HomeViewDirectorChangeButton extends React.Component<
  HomeViewDirectorChangeButtonProps,
  {}
> {
  render() {
    return (
      <TouchableOpacity
        style={this.styles.button}
        onPress={() => this.props.onPress()}>
        <Icon name="megaphone" size={25} color="#FFF" />
        <Text style={this.styles.text}>디렉터 지원하기</Text>
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    button: {
      height: 44,
      backgroundColor: '#4f6cff',
      flexDirection: 'row',
      justifyContent: 'center',
      alignItems: 'center',
      borderRadius: 20,
    },
    text: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 5,
      //   marginBottom: 10,
      marginLeft: 8,
      color: '#FFF',
    },
  });
}
