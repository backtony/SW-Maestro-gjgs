import InfoBox from '@/components/InfoBox';
import Zone from '@/utils/Zone';
import React from 'react';
import {StyleSheet, Text, View} from 'react-native';

interface LectureDetailViewInfoBlockProps {
  lecture: any;
}

export default class LectureDetailViewInfoBlock extends React.Component<
  LectureDetailViewInfoBlockProps,
  {}
> {
  render() {
    return (
      <View>
        <Text style={this.styles.text1}>클래스 요약</Text>
        <View style={this.styles.wrapper1}>
          <InfoBox title={'위치'}>
            <Text style={this.styles.text2}>
              {Zone.getZone(this.props.lecture.zoneId)[1]}
            </Text>
          </InfoBox>

          <InfoBox title={'인원'}>
            <Text style={this.styles.text3}>
              {`${
                this.props.lecture && this.props.lecture.minParticipants
                  ? this.props.lecture.minParticipants
                  : '2'
              } ~ ${
                this.props.lecture && this.props.lecture.maxParticipants
                  ? this.props.lecture.maxParticipants
                  : '4'
              } 명`}
            </Text>
          </InfoBox>
        </View>

        <View style={this.styles.wrapper2}>
          <InfoBox title={'진행시간'}>
            <Text style={this.styles.text4}>
              {`${this.props.lecture.progressTime}시간`}
            </Text>
          </InfoBox>

          <InfoBox title={'평점'}>
            <View style={{flexDirection: 'row'}}>
              <Text style={this.styles.text5}>★ 4.5</Text>
              <Text style={this.styles.text6}>점</Text>
            </View>
          </InfoBox>
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    text1: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 40,
      marginBottom: 20,
      marginHorizontal: 20,
    },
    wrapper1: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      marginHorizontal: 20,
    },
    text2: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#1d1d1f',
    },
    text3: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#1d1d1f',
    },
    wrapper2: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      marginTop: 15,
      marginHorizontal: 20,
    },
    text4: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#1d1d1f',
    },
    text5: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#4f6cff',
    },
    text6: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#1d1d1f',
    },
  });
}
