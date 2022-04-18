import Category from '@/utils/Category';
import {englishDay, englishTime} from '@/utils/commonParam';
import Zone from '@/utils/Zone';
import React from 'react';
import {StyleSheet} from 'react-native';
import {Text, View} from 'react-native';
import InfoBox from '@/components/InfoBox';

interface GroupDetailInfoProps {
  team: any;
}

export default class GroupDetailInfo extends React.Component<
  GroupDetailInfoProps,
  {}
> {
  render() {
    return (
      <View>
        <Text style={this.styles.mainTitle}>그룹 요약</Text>
        <View style={this.styles.upperBlock}>
          <InfoBox title={'위치'}>
            <Text style={this.styles.text1}>
              {this.props.team
                ? Zone.getZone(this.props.team.zoneId)[1]
                : '홍대'}
            </Text>
          </InfoBox>
          <InfoBox title={'인원'}>
            <View style={this.styles.wrapper}>
              <Text style={this.styles.text2}>
                {this.props.team ? this.props.team.applyPeople : 2}
              </Text>
              <Text style={this.styles.text3}>
                {`/${this.props.team ? this.props.team.maxPeople : 4}`}
              </Text>
            </View>
          </InfoBox>
        </View>

        <View style={this.styles.wrapper2}>
          <InfoBox title={'요일'}>
            <Text style={this.styles.text4}>
              {this.props.team
                ? this.props.team.day
                    .split('|')
                    .map((day: string) => englishDay[day])
                    .join('·')
                : '토·일'}
            </Text>
          </InfoBox>
          <InfoBox title={'시간'}>
            <Text style={this.styles.text5}>
              {this.props.team
                ? this.props.team.time
                    .split('|')
                    .map((time: string) => englishTime[time])
                    .join('·')
                : '오후'}
            </Text>
          </InfoBox>
        </View>
        <View style={this.styles.wrapper3}>
          <Text style={this.styles.text6}>공통태그</Text>
          <Text style={this.styles.text7}>
            {this.props.team
              ? this.props.team.categoryList
                  .map(id => `#${Category.getCategory(id)[1]}`)
                  .join(' ')
              : '#다이빙 #보드'}
          </Text>
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    mainTitle: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 20,
      marginBottom: 10,
    },
    upperBlock: {flexDirection: 'row', justifyContent: 'space-between'},
    text1: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#1d1d1f',
    },
    wrapper: {flexDirection: 'row'},
    text2: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#4f6cff',
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
      color: '#1d1d1f',
    },
    wrapper3: {
      width: '100%',
      backgroundColor: '#fafafb',
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
      padding: 12,
      borderRadius: 6,
      marginTop: 17,
    },
    text6: {
      fontSize: 12,
      lineHeight: 14,
      fontFamily: 'NotoSansCJKkr-Regular',
      color: '#8e8e8f',
    },
    text7: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#1d1d1f',
    },
  });
}
