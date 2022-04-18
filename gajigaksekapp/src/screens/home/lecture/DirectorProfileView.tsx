import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {Image, ScrollView, StyleSheet, Text, View} from 'react-native';
import {Button} from 'react-native-elements';
import {SceneMap, TabBar, TabView} from 'react-native-tab-view';
import Icon from 'react-native-vector-icons/Ionicons';
import DirectorClassTab from './tab/DirectorClassTab';
import DirectorReviewTab from './tab/DirectorReviewTab';

interface DirectorProfileViewProps {
  navigation: any;
}

interface DirectorProfileViewStates {
  width: number;
}

export default class DirectorProfileView extends React.Component<
  DirectorProfileViewProps,
  DirectorProfileViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {width: 0};
  }
  render() {
    return (
      <ScrollView style={{backgroundColor: '#FFF'}}>
        <Image
          style={[this.styles.header]}
          source={require('gajigaksekapp/src/asset/profileHeaderImage.jpg')}
        />
        <View style={this.styles.headerTitleWrapper}>
          <Button
            icon={<Icon name="chevron-back" size={24} color="#FFF" />}
            buttonStyle={{backgroundColor: '(0, 0, 0, 0.5)'}}
            onPress={() => this.props.navigation.goBack()}
          />
          <Text style={this.styles.headerTitle}>
            {this.props.route.params.nickname}
          </Text>
        </View>
        <Image
          style={this.styles.avatar}
          source={{
            uri: this.props.route.params.image,
          }}
        />

        <View
          style={this.styles.body}
          onLayout={event => {
            var {width} = event.nativeEvent.layout;
            this.setState({width});
            console.log('width: ', width);
          }}>
          <View style={this.styles.bodyContent}>
            <Text style={this.styles.name}>
              {this.props.route.params.nickname}
            </Text>
            <Text style={this.styles.text}>소개</Text>
            <View
              style={{padding: 20, backgroundColor: '#fafafb', marginTop: 20}}>
              <Text style={this.styles.text2}>
                {this.props.route.params.info}
              </Text>
            </View>
          </View>

          <TabView
            renderTabBar={props => (
              <TabBar
                style={{backgroundColor: 'white'}}
                indicatorStyle={[
                  this.styles.indicator,
                  {marginHorizontal: (this.state.width / 2 - 40) / 2},
                  ,
                ]}
                renderLabel={({route, focused, color}) => (
                  <Text
                    style={[
                      this.styles.text3,
                      focused ? {color: '#4f6cff'} : {},
                    ]}>
                    {route.title}
                  </Text>
                )}
                {...props}
              />
            )}
            style={{height: 500, marginTop: 36}}
            navigationState={{
              index: 0,
              routes: [
                {key: 'class', title: '클래스'},
                {key: 'review', title: '리뷰'},
              ],
            }}
            onIndexChange={index => {
              this.setState({tabIndex: index});
            }}
            renderScene={SceneMap({
              class: () => (
                <DirectorClassTab
                  navigation={this.props.navigation}
                  directorId={this.props.route.params.directorId}
                />
              ),
              review: () => (
                <DirectorReviewTab
                  navigation={this.props.navigation}
                  directorId={this.props.route.params.directorId}
                />
              ),
            })}
          />
        </View>
      </ScrollView>
    );
  }

  private styles = StyleSheet.create({
    header: {
      backgroundColor: '#00BFFF',
      height: 140 + STATUSBAR_HEIGHT,
    },
    headerTitleWrapper: {
      marginTop: STATUSBAR_HEIGHT,
      height: 60,
      width: '100%',
      flexDirection: 'row',
      justifyContent: 'flex-start',
      alignItems: 'center',
      position: 'absolute',
    },

    headerTitle: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 20,
      color: '#FFF',
    },
    text: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 16,
      lineHeight: 18,
      color: '#1d1d1f',
      alignSelf: 'flex-start',
      marginLeft: 20,
      marginTop: 40,
    },
    text2: {
      fontSize: 14,
      lineHeight: 22,
      fontFamily: 'NotoSansCJKkr-Regular',
    },
    text3: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Regular',
      color: '#8e8e8f',
    },
    indicator: {
      backgroundColor: '#4f6cff',
      height: 4,
      width: 40,
      alignSelf: 'center',
      borderRadius: 2,
    },
    avatar: {
      width: 120,
      height: 120,
      borderRadius: 63,
      borderWidth: 2,
      borderColor: 'white',
      marginBottom: 10,
      alignSelf: 'center',
      position: 'absolute',
      marginTop: 80 + STATUSBAR_HEIGHT,
    },
    body: {
      marginTop: 40,
    },
    bodyContent: {
      flex: 1,
      alignItems: 'center',
    },
    smallInfoBlock: {
      margin: 15,
      flexDirection: 'row',
      justifyContent: 'space-around',
      alignItems: 'center',
      height: 70,
      backgroundColor: '#fafafb',
      borderRadius: 10,
      width: 350,
    },
    smallInfo: {
      marginHorizontal: 30,
      marginVertical: 20,
      fontSize: 28,
      color: '#696969',
      fontWeight: '600',
    },
    name: {
      fontSize: 24,
      lineHeight: 28,
      color: '#1d1d1f',
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 35,
    },
    info: {
      fontSize: 16,
      color: '#00BFFF',
      marginTop: 10,
    },
    description: {
      fontSize: 16,
      color: '#696969',
      marginTop: 10,
      textAlign: 'center',
    },
    buttonContainer: {
      marginTop: 10,
      height: 45,
      flexDirection: 'row',
      justifyContent: 'center',
      alignItems: 'center',
      marginBottom: 20,
      width: 250,
      borderRadius: 30,
      backgroundColor: '#00BFFF',
    },
    hheader: {
      marginVertical: 20,
      fontSize: 28,
      fontWeight: '600',
      alignSelf: 'flex-start',
    },
    textBlock: {
      width: '100%',
      backgroundColor: '#bdbdbd',
      borderRadius: 5,
      flexDirection: 'row',
    },
    hashText: {
      margin: 10,
      fontSize: 18,
    },
  });
}
