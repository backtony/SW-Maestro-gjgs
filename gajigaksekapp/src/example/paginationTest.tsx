import * as React from 'react';
import {Image} from 'react-native';
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  StatusBar,
  FlatList,
  TouchableOpacity,
} from 'react-native';
import alarmApi from '../services/apis/alarmApi';

interface settingPageState {
  loading: boolean;
  randomUserData: any[];
  loadingExtraData: boolean;
  page: number;
}

class paginationPage extends React.Component<{}, settingPageState> {
  constructor(props: any) {
    super(props);
    this.state = {
      loading: true,
      randomUserData: [],
      loadingExtraData: false,
      page: 1,
    };
  }

  private LoadRandomData = () => {
    fetch(`https://randomuser.me/api/?results=10&page=${this.state.page}`)
      .then(response => response.json())
      .then(responseJson => {
        this.setState({
          randomUserData:
            this.state.page === 1
              ? responseJson.results
              : [...this.state.randomUserData, ...responseJson.results],
        });
      })
      .catch(error => {
        console.log('Error selecting random data: ' + error);
      });
  };
  componentDidMount() {
    this.LoadRandomData();
  }

  private renderCustomItem = ({item, index}) => {
    return (
      <View style={{justifyContent: 'center', alignItems: 'center'}}>
        <Text>{item.gender}</Text>
        <Text>
          {item.name.first} {item.name.last}
        </Text>
        <Image
          source={{uri: item.picture.medium}}
          style={{width: 200, height: 200}}
        />
      </View>
    );
  };

  private LoadMoreRandomData = () => {
    this.setState(
      {
        page: this.state.page + 1,
      },
      () => this.LoadRandomData(),
    );
  };

  render() {
    return (
      <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
        <FlatList
          style={this.styles.flatList}
          data={this.state.randomUserData}
          renderItem={this.renderCustomItem}
          keyExtractor={(item, index) => item.email}
          onEndReachedThreshold={0}
          onEndReached={this.LoadMoreRandomData}>
          <Text>고객센터</Text>
        </FlatList>
      </View>
    );
  }

  private styles = StyleSheet.create({
    flatList: {
      width: '100%',
      padding: 10,
    },
    block: {
      height: 50,
      width: '100%',
      backgroundColor: '#5d5d67',
      marginTop: 10,
      borderRadius: 10,
      justifyContent: 'center',
      alignItems: 'center',
    },
  });
}

export default paginationPage;
