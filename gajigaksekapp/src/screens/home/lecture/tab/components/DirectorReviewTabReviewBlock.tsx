import React from 'react';
import {Image, StyleSheet, Text, View} from 'react-native';

interface DirectorReviewTabReviewBlockProps {
  lecture: any;
}

export default class DirectorReviewTabReviewBlock extends React.Component<
  DirectorReviewTabReviewBlockProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.container}>
        <View style={{flexDirection: 'row', marginBottom: 10}}>
          <Image
            source={{uri: this.props.lecture.reviewImageFileUrl}}
            style={this.styles.img}
          />
          <View>
            <Text
              style={this.styles.text1}>{`★ ${this.props.lecture.score}`}</Text>
            <Text style={this.styles.text2}>{this.props.lecture.nickname}</Text>
          </View>
        </View>
        <Text style={this.styles.text3}>{this.props.lecture.text}</Text>
        <Image
          source={{uri: this.props.lecture.reviewImageFileUrl}}
          style={this.styles.img2}
        />
        {this.props.lecture.replyText && (
          <View style={{marginTop: 20}}>
            <Text style={this.styles.text4}>디렉터 답변 :</Text>
            <Text>{this.props.lecture.replyText}</Text>
          </View>
        )}
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {
      flex: 1,
      padding: 20,
      backgroundColor: '#fafafb',
      borderRadius: 6,
    },
    img: {
      width: 60,
      height: 60,
      borderWidth: 2,
      borderRadius: 30,
      borderColor: '#ffffff',
      marginRight: 10,
    },
    text1: {
      color: '#4f6cff',
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 16,
    },
    text2: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 16,
      fontWeight: 'bold',
      fontStyle: 'normal',
      lineHeight: 20,
      letterSpacing: 0,
      color: '#1d1d1f',
    },
    text3: {
      fontFamily: 'NotoSansCJKkr-Regular',
      fontSize: 12,
      fontWeight: 'normal',
      fontStyle: 'normal',
      lineHeight: 18,
      letterSpacing: 0,
      color: '#1d1d1f',
    },
    img2: {
      width: 90,
      height: 90,
      borderRadius: 10,
      marginTop: 10,
    },
    text4: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 16,
      fontWeight: 'bold',
      fontStyle: 'normal',
      lineHeight: 20,
      letterSpacing: 0,
      color: '#1d1d1f',
    },
  });
}
