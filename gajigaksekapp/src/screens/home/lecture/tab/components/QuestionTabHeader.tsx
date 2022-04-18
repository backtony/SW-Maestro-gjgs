import React from 'react';
import {StyleSheet, Text, View} from 'react-native';
import {Button} from 'react-native-elements/dist/buttons/Button';

interface QuestionTabHeaderProps {
  counts: number;
  onPress: () => void;
}

export default class QuestionTabHeader extends React.Component<
  QuestionTabHeaderProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.wrapper1}>
        <View style={{flexDirection: 'row'}}>
          <Text style={this.styles.text1}>문의</Text>
          <Text style={this.styles.text2}>{`(${this.props.counts})`}</Text>
        </View>

        <Button
          buttonStyle={this.styles.button}
          titleStyle={this.styles.title}
          onPress={() => this.props.onPress()}
          title={'더보기'}
        />
      </View>
    );
  }

  private styles = StyleSheet.create({
    wrapper1: {
      flexDirection: 'row',
      marginTop: 20,
      alignItems: 'center',
      marginBottom: 20,
      justifyContent: 'space-between',
    },
    text1: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 7,
      marginLeft: 6,
    },
    text2: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 7,
      color: '#4f6cff',
    },
    button: {
      height: 28,
      borderRadius: 6,
      backgroundColor: 'transparent',
      paddingTop: 8,
      paddingBottom: 5,
    },
    title: {
      color: '#4f6cff',
      fontSize: 12,
      lineHeight: 14,
      fontFamily: 'NotoSansCJKkr-Regular',
    },
  });
}
