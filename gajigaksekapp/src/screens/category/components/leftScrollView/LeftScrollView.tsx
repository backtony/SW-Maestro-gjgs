import React from 'react';
import {ScrollView, StyleSheet} from 'react-native';
import LeftScrollViewButton from './LeftScrollViewButton';

interface LeftScrollViewProps {
  rightScrollView: ScrollView | null;
  mainCategory: string;
  yCord: any;
  setMainCategory: (mainCategory: string) => void;
}

export default class LeftScrollView extends React.Component<
  LeftScrollViewProps,
  {}
> {
  render() {
    return (
      <ScrollView
        style={this.leftScrollViewStyle.scrollView}
        showsVerticalScrollIndicator={false}
        showsHorizontalScrollIndicator={false}>
        {buttonInfo.map(buttonInfo => {
          return (
            <LeftScrollViewButton
              key={buttonInfo.title}
              mainCategory={this.props.mainCategory}
              rightScrollView={this.props.rightScrollView}
              buttonInfo={buttonInfo}
              setMainCategory={this.props.setMainCategory}
              yCord={this.props.yCord}
            />
          );
        })}
      </ScrollView>
    );
  }
  private leftScrollViewStyle = StyleSheet.create({
    scrollView: {width: 60, backgroundColor: '#FFF', paddingTop: 10},
  });
}

const buttonInfo = [
  {
    imageSource: require('gajigaksekapp/src/asset/iconImage/pin.png'),
    title: '지역별',
    width: 14,
    height: 28,
  },
  {
    imageSource: require('gajigaksekapp/src/asset/iconImage/activity.png'),
    title: '액티비티',
    width: 20,
    height: 20,
  },
  {
    imageSource: require('gajigaksekapp/src/asset/iconImage/cooking.png'),
    title: '쿠킹',
    width: 20,
    height: 20,
  },
  {
    imageSource: require('gajigaksekapp/src/asset/iconImage/beautyHealth.png'),
    title: '뷰티/헬스',
    width: 20,
    height: 20,
  },
  {
    imageSource: require('gajigaksekapp/src/asset/iconImage/dance.png'),
    title: '댄스',
    width: 20,
    height: 20,
  },
  {
    imageSource: require('gajigaksekapp/src/asset/iconImage/art.png'),
    title: '미술',
    width: 20,
    height: 20,
  },
  {
    imageSource: require('gajigaksekapp/src/asset/iconImage/handcraft.png'),
    title: '수공예',
    width: 20,
    height: 20,
  },
  {
    imageSource: require('gajigaksekapp/src/asset/iconImage/music.png'),
    title: '음악/예술',
    width: 20,
    height: 20,
  },
];
