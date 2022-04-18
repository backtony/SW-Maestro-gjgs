import React from 'react';
import {StyleSheet, Text, View} from 'react-native';

interface LectureDetailViewPeopleBlockProps {
  lecture: any;
}

export default class LectureDetailViewPeopleBlock extends React.Component<
  LectureDetailViewPeopleBlockProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.container}>
        <View style={this.styles.textWrapper}>
          <Text style={this.styles.text1}>1인</Text>
          <Text style={this.styles.text2}>
            {`${this.props.lecture.priceOne}원`}
          </Text>
        </View>
        <View style={this.styles.textWrapper}>
          <Text style={this.styles.text1}>2인</Text>
          <Text style={this.styles.text2}>
            {`${this.props.lecture.priceTwo}원`}
          </Text>
        </View>
        <View style={this.styles.textWrapper}>
          <Text style={this.styles.text1}>3인</Text>
          <Text style={this.styles.text2}>
            {`${this.props.lecture.priceThree}원`}
          </Text>
        </View>
        <View style={this.styles.textWrapper}>
          <Text style={this.styles.text1}>4인</Text>
          <Text style={this.styles.text2}>
            {`${this.props.lecture.priceFour}원`}
          </Text>
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {
      width: '90%',
      backgroundColor: '#fafafb',
      padding: 12,
      borderRadius: 6,
      marginTop: 17,
      marginHorizontal: 20,
    },
    textWrapper: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      width: '100%',
      marginVertical: 15,
    },
    text1: {
      fontSize: 14,
      lineHeight: 16,
      fontFamily: 'NotoSansCJKkr-Regular',
      color: '#8e8e8f',
    },
    text2: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#1d1d1f',
    },
  });
}
