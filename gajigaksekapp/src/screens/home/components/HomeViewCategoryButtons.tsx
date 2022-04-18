import React from 'react';
import {StyleSheet, View} from 'react-native';
import HomeViewCategoryButton from './HomeViewCategoryButton';

interface HomeViewCategoryButtonsProps {
  navigation: any;
}

export default class HomeViewCategoryButtons extends React.Component<
  HomeViewCategoryButtonsProps,
  {}
> {
  render() {
    return (
      <View>
        <View style={this.styles.buttons}>
          <HomeViewCategoryButton
            navigation={this.props.navigation}
            title={'액티비티'}
            imgSrc={
              'https://cdn.zeplin.io/60f22448a6ebf90ffb53be83/assets/8A4B06E8-0E54-4880-BBD9-FB8BD77C643E.png'
            }
          />
          <HomeViewCategoryButton
            navigation={this.props.navigation}
            title={'쿠킹'}
            imgSrc={
              'https://cdn.zeplin.io/60f22448a6ebf90ffb53be83/assets/7F93ED6C-D0B6-4DFC-8206-695D423B192C.png'
            }
          />
          <HomeViewCategoryButton
            navigation={this.props.navigation}
            title={'뷰티/헬스'}
            imgSrc={
              'https://cdn.zeplin.io/60f22448a6ebf90ffb53be83/assets/C2C9E5A8-39F5-46BE-B336-D3D7E8B46F73.png'
            }
          />
          <HomeViewCategoryButton
            navigation={this.props.navigation}
            title={'댄스'}
            imgSrc={
              'https://cdn.zeplin.io/60f22448a6ebf90ffb53be83/assets/9E6C4FDA-BA7B-40DE-979A-12B2FA2D6C04.png'
            }
          />
        </View>

        <View style={this.styles.buttons2}>
          <HomeViewCategoryButton
            navigation={this.props.navigation}
            title={'수공예'}
            imgSrc={
              'https://git.swmgit.org/swm-12/12_swm18/gajigaksek-web/uploads/ecfed6c5da47c0f7ae947ae6686ca386/wool-ball.png'
            }
          />
          <HomeViewCategoryButton
            navigation={this.props.navigation}
            title={'미술'}
            imgSrc={
              'https://cdn.zeplin.io/60f22448a6ebf90ffb53be83/assets/DAA1C3B3-8EAF-420E-8841-EF5D9A460F70.png'
            }
          />
          <HomeViewCategoryButton
            navigation={this.props.navigation}
            title={'음악/예술'}
            imgSrc={
              'https://cdn.zeplin.io/60f22448a6ebf90ffb53be83/assets/AC51725C-64DE-444E-AE37-8E4B3DA6A07D.png'
            }
          />
          <HomeViewCategoryButton
            navigation={this.props.navigation}
            title={'자유주제'}
            imgSrc={
              'https://cdn.zeplin.io/60f22448a6ebf90ffb53be83/assets/B0A1D2F4-307B-4802-9C99-4B0463CC1856.png'
            }
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    buttons: {
      marginTop: 40,
      flexDirection: 'row',
      justifyContent: 'space-between',
    },
    buttons2: {
      marginTop: 20,
      flexDirection: 'row',
      justifyContent: 'space-between',
    },
  });
}
