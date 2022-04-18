import InfoBox from '@/components/InfoBox';
import Category from '@/utils/Category';
import {getDayOfWeekInKorean, getTimeInKorean} from '@/utils/commonFunctions';
import {englishAge} from '@/utils/commonParam';
import Zone from '@/utils/Zone';
import React from 'react';
import {StyleSheet, Text, View} from 'react-native';

interface BulletinDetailViewBlocksProps {
  team: any;
}

export default class BulletinDetailViewBlocks extends React.Component<
  BulletinDetailViewBlocksProps,
  {}
> {
  render() {
    return (
      <View>
        <Text style={this.styles.title}>모집 요약</Text>
        <View style={this.styles.line1}>
          <InfoBox title={'위치'}>
            <Text style={this.styles.locationText}>
              {this.props.team
                ? Zone.getZone(
                    this.props.team.bulletinsLecture.lecturesZoneId,
                  )[1]
                : '홍대'}
            </Text>
          </InfoBox>

          <InfoBox title={'연령'}>
            <Text style={this.styles.ageText}>
              {this.props.team ? englishAge[this.props.team.age] : '20~25세'}
            </Text>
          </InfoBox>
        </View>

        <View style={this.styles.line2}>
          <InfoBox title={'요일·시간'}>
            <Text style={this.styles.dayTimeText}>
              {this.props.team
                ? this.props.team.day
                    .split('|')
                    .map(value => getDayOfWeekInKorean(value))
                    .join(',')
                : '토, 일'}
              {'·'}
              {this.props.team
                ? this.props.team.time
                    .split('|')
                    .map(value => getTimeInKorean(value))
                    .join('/')
                : '오후'}
            </Text>
          </InfoBox>

          <InfoBox title={'인원'}>
            <View style={{flexDirection: 'row'}}>
              <Text style={this.styles.peopleText}>
                {this.props.team
                  ? this.props.team.bulletinsTeam.currentPeople
                  : 2}
              </Text>
              <Text style={this.styles.peopleText2}>
                {`/${
                  this.props.team ? this.props.team.bulletinsTeam.maxPeople : 3
                }`}
              </Text>
            </View>
          </InfoBox>
        </View>

        <View style={this.styles.tagWrapper}>
          <Text style={this.styles.tagTitle}>공통태그</Text>
          <Text style={this.styles.tagText}>
            {this.props.team
              ? `# ${
                  Category.getCategory(
                    this.props.team.bulletinsLecture.lecturesCategoryId,
                  )[1]
                }`
              : '#다이빙 #보드'}
          </Text>
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    title: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 20,
      marginBottom: 10,
    },
    locationText: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#1d1d1f',
    },
    ageText: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#1d1d1f',
    },
    line1: {flexDirection: 'row', justifyContent: 'space-between'},
    line2: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      marginTop: 15,
    },
    dayTimeText: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#1d1d1f',
    },
    peopleText: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#4f6cff',
    },
    peopleText2: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#1d1d1f',
    },
    tagWrapper: {
      width: '100%',
      backgroundColor: '#fafafb',
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
      padding: 12,
      borderRadius: 6,
      marginTop: 17,
    },
    tagTitle: {
      fontSize: 12,
      lineHeight: 14,
      fontFamily: 'NotoSansCJKkr-Regular',
      color: '#8e8e8f',
    },
    tagText: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#1d1d1f',
    },
  });
}
