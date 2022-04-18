import React from 'react';
import {StyleSheet, Text, TouchableOpacity} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

interface QuestionTabWriteButtonProps {
  onPress: () => void;
}

export default class QuestionTabWriteButton extends React.Component<
  QuestionTabWriteButtonProps,
  {}
> {
  render() {
    return (
      <TouchableOpacity
        style={this.styles.button}
        onPress={() => this.props.onPress()}>
        <Icon name="paper-plane" size={20} color="#FFF" />
        <Text style={this.styles.text}>문의 작성</Text>
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    button: {
      padding: 12,
      backgroundColor: '#4f6cff',
      borderRadius: 6,
      flexDirection: 'row',
      justifyContent: 'center',
      alignItems: 'center',
      marginTop: 10,
      marginBottom: 20,
    },
    text: {
      fontSize: 16,
      fontWeight: 'bold',
      color: '#FFF',
      marginLeft: 4,
    },
  });
}
